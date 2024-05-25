package com.example.twohand_project.Model;

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.MemoryCacheSettings;
import com.google.firebase.firestore.MemoryLruGcSettings;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class FirebaseModel {
    private FirebaseFirestore db;

    private FirebaseStorage storage;

    FirebaseModel(){
        db=FirebaseFirestore.getInstance();
        MemoryCacheSettings memoryCacheSettings = MemoryCacheSettings.newBuilder()
                .setGcSettings(MemoryLruGcSettings.newBuilder()
                        .setSizeBytes(0) // Set the cache size to 0 to disable it
                        .build())
                .build();

        // Create Firestore settings to use in-memory cache settings
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setLocalCacheSettings(memoryCacheSettings)
                .build();

        // Apply settings to Firestore instance
        db.setFirestoreSettings(settings);
        storage= FirebaseStorage.getInstance();

    };
    public void getAllPostsSince(Long since,Model.Listener<List<Post>> callback) {
        db.collection("Post").whereGreaterThanOrEqualTo("lastUpdated",new Timestamp(since,0)).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            List<Post> data=new ArrayList<>();
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for ( DocumentSnapshot document:task.getResult()){
                        data.add(Post.fromJson(document.getData()));
                    }
                    callback.onComplete(data);
                }

            }
        });

    }
    public void addPost(Post post, Model.Listener<Void> listener) {
        db.collection("Post").add(Post.toJson(post)).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                listener.onComplete(null);
            }
        });
    }
    public void getPostsByCategories(Long since,String clothKind, String color, Model.Listener<List<Post>> listener) {
        db.collection("Post").whereEqualTo("color",color).whereEqualTo("kind",clothKind).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Post> data=new ArrayList<>();
                if (task.isSuccessful()){
                    for(DocumentSnapshot document:task.getResult()){
                        data.add(Post.fromJson(document.getData()));
                    }
                    listener.onComplete(data);
                }
            }
        });
    }
    public void getPostById(String id, Model.Listener<Post> listener) {
        db.collection("Post").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful())
                    listener.onComplete(Post.fromJson(task.getResult().getData()));
            }
        });


    }
    void uploadPhoto(String id, Bitmap bitmap, Model.Listener<String> listener){

    StorageReference storageRef = storage.getReference();
    StorageReference imagesRef = storageRef.child("images/"+id+".jpg");
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
    byte[] data = baos.toByteArray();

    UploadTask uploadTask = imagesRef.putBytes(data);
    uploadTask.addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception exception) {
            listener.onComplete(null);
        }
        }
    ).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    listener.onComplete(uri.toString());
                }
            });
        }
    });}
}
