package com.example.twohand_project.Model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.MemoryCacheSettings;
import com.google.firebase.firestore.MemoryLruGcSettings;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
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
                        .setSizeBytes(0)
                        .build())
                .build();

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setLocalCacheSettings(memoryCacheSettings)
                .build();

        db.setFirestoreSettings(settings);
        storage= FirebaseStorage.getInstance();
        mAuth=FirebaseAuth.getInstance();
    };
    public void getAllUsersSince(Long since, Model.Listener<List<User>> callback) {
        db.collection("User").whereGreaterThan("lastUpdated",new Timestamp(since,0)).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            List<User> data=new ArrayList<>();
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for ( DocumentSnapshot document:task.getResult()){
                        data.add(User.fromJson(document.getData()));
                    }
                    callback.onComplete(data);
                }

            }
        });
    }
    public void getAllPostsSince(Long since,Model.Listener<List<Post>> callback) {
        db.collection("Post").whereGreaterThan("lastUpdated",new Timestamp(since,0)).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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


    public void getLoggedUser(Model.Listener<User> listener){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.d("TAG",currentUser.getEmail());
        db.collection("User").whereEqualTo("email",currentUser.getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                User user=User.fromJson(task.getResult().getDocuments().get(0).getData());


                listener.onComplete(user);
            }
        });


    }


    public void register(User newUser,String password){
        db.collection("User").document(newUser.username).set(User.toJson(newUser)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mAuth.createUserWithEmailAndPassword(newUser.email, password)
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder().setDisplayName(newUser.username).build();
                                mAuth.getCurrentUser().updateProfile(profile);
                                Model.instance().username= newUser.username;


                                mAuth.signInWithEmailAndPassword(newUser.email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        Model.instance().refreshAllUsers();

                                    }
                                });
                            }
                        });

            }
        });
    }

    public void isEmailTaken(String email, Model.Listener<Boolean> listener) {
        db.collection("User").whereEqualTo("email",email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    listener.onComplete(task.getResult().getDocuments().size()>0);
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


    public void updateFavorites(User user) {
        db.collection("User").document(user.username).update(User.toJson(user))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                });

    }

    public void updatePost(Post post, Model.Listener<Void> listener) {

        db.collection("Post").document(post.id).update(Post.toJson(post)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Model.instance().refreshAllPosts();
                listener.onComplete(null);
            }
        });
    }

    public void deletePost(Post post) {
        db.collection("Post").document(post.id).delete();
    }

    public void updateUserAndHisPosts(User user, Model.Listener<Void> listener) {
        String username = user.username;
        String newUserNumber = user.number;
        String newPhotoImg=user.userImg;
        String location=user.location;
        db.collection("User").document(username).update(User.toJson(user)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                db.collection("Post")
                        .whereEqualTo("owner", username)
                        .get()
                        .addOnCompleteListener(task2 -> {
                            if (task2.isSuccessful()) {
                                WriteBatch batch = db.batch();
                                for (DocumentSnapshot document : task2.getResult()) {
                                    DocumentReference postRef = document.getReference();
                                    batch.update(postRef, "number", newUserNumber);
                                    batch.update(postRef,"ownerImg",newPhotoImg);
                                    batch.update(postRef,"location",location);
                                }

                                batch.commit().addOnCompleteListener(batchTask -> {
                                    if (batchTask.isSuccessful()) {
                                        Model.instance().refreshAllUsers();
                                        listener.onComplete(null);
                                        Log.d("TAG", "Batch update successful.");
                                    } else {
                                        // Handle failure
                                        Log.w("TAG", "Batch update failed.", batchTask.getException());
                                    }
                                });
                            } else {
                                // Handle error in retrieving documents
                                Log.w("TAG", "Error getting documents.", task2.getException());
                            }
                        });
            }
        });

    }



    public void logOut() {
        mAuth.signOut();
    }

    public String getLoggedUserUsername() {
        String username=mAuth.getCurrentUser().getDisplayName();

        return username;
    }



}
