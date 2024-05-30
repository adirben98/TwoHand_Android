package com.example.twohand_project.Model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private FirebaseAuth mAuth;

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
        mAuth=FirebaseAuth.getInstance();
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
        db.collection("Post").document(post.id).set(Post.toJson(post)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Model.instance().refreshAllPosts();
                listener.onComplete(null);
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
    public boolean isLoggedIn(){

        FirebaseUser currentUser = mAuth.getCurrentUser();
        return currentUser!=null;
    }

    public void logIn(String username, String password, Model.Listener<Boolean> listener) {
        db.collection("User").document(username).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    User user = User.fromJson(task.getResult().getData());
                    mAuth.signInWithEmailAndPassword(user.email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    listener.onComplete(task.isSuccessful());
                                }
                            });
                } else {
                    listener.onComplete(task.isSuccessful());
                }
            }
        });
    }


    public LiveData<User> getLoggedUser(){
        MutableLiveData<User> user=new MutableLiveData<>();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.d("TAG",currentUser.getEmail());
        db.collection("User").whereEqualTo("email",currentUser.getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for(DocumentSnapshot document:task.getResult())
                        Model.instance().mainHandler.post(()->user.setValue(User.fromJson(document.getData())));
            }
        });
        return user;
    }
    public void register(User newUser,String password,Model.Listener<Void> listener){
        db.collection("User").document(newUser.username).set(User.toJson(newUser)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mAuth.createUserWithEmailAndPassword(newUser.email, password)
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                listener.onComplete(null);
                            }
                        });

            }
        });
    }

    public void isEmailTaken(String username, Model.Listener<Boolean> listener) {
        db.collection("User").document(username).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    listener.onComplete(task.getResult().getData()!=null);
                }
            }
        });
    }

    public void isUsernameTaken(String username, Model.Listener<Boolean> listener) {
        db.collection("User").document(username).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    listener.onComplete(task.getResult().getData()!=null);
                }
            }
        });
    }

    public void getUserPosts(User user,Model.Listener<List<Post>> listener) {
            db.collection("Post").whereEqualTo("owner",user.username).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    List<Post> data=new ArrayList<>();
                    for(DocumentSnapshot document:task.getResult()){
                        data.add(Post.fromJson(document.getData()));
                    }
                    listener.onComplete(data);
                }
            });


    }

    public void getOutUserPosts(String username, Model.Listener<List<Post>> listener) {
    }


    public void getFavorites(String username,Model.Listener<List<String>> listener) {
        db.collection("User").document(username).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                listener.onComplete(User.fromJson(documentSnapshot.getData()).favorites);
            }
        });
    }

    public void updateFavorites(User user) {
        db.collection("User").document(user.username).update(User.toJson(user))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Model.instance().refreshAllPosts();
                    }
                });

    }
}
