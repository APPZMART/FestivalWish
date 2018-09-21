package com.autodidact.developers.festivalwish;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.List;

import static com.autodidact.developers.festivalwish.ImageListActivity.mStorageRef;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private static List<ImageUpload> listImage;
    private static Context context;

    public static int fileflag = 0;
    int REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    File storageDir = null;

    public RecyclerAdapter(Context context, List<ImageUpload> listImage) {
        this.listImage= listImage;
        this.context = context;

    }
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate( R.layout.image_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView itemimage;


        public ViewHolder(View view) {
            super(view);



            final FloatingActionButton myFab = (FloatingActionButton) view.findViewById(R.id.ToShare);
            final FloatingActionButton downmyFab = (FloatingActionButton) view.findViewById(R.id.ToDownload);
            final FloatingActionButton whatsappmyFab = (FloatingActionButton) view.findViewById(R.id.Towhatsapp);
            myFab.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    View parentRow = (View) v.getParent();
                    RecyclerView recyclerView = (RecyclerView) parentRow.getParent();
                    String strexttemp = null;
                    int pos = getAdapterPosition();
                    ImageUpload mylistname = listImage.get(pos);
                    final String filename = mylistname.getName();
                    String imageurl = mylistname.getUrl();
                    String[] parts = imageurl.split("\\%2F");
                    String temp = parts[1].trim();
                    String[] imagenameTemp = temp.split("\\?");
                    String imagename = imagenameTemp[0].trim();
                    String finalimagename=imagename.replaceAll("%20"," ");
                    String[] fileexttemp = finalimagename.split("\\.");
                    String fileext = fileexttemp[1].trim();
                    String newfilename = filename+"."+fileext;
                    ShareFile(finalimagename, newfilename);
                }
            });

            downmyFab.setOnClickListener(new View.OnClickListener() {


                public void onClick(View v) {
//                    FileOutputStream fos = null;
                    storageDir = null;
                    View parentRow = (View) v.getParent();
                    RecyclerView recyclerView = (RecyclerView) parentRow.getParent();
                    String strexttemp = null;
                    int pos = getAdapterPosition();
                    ImageUpload mylistname = listImage.get(pos);
                    final String filename = mylistname.getName();
                    String imageurl = mylistname.getUrl();
                    String[] parts = imageurl.split("\\%2F");
                    String temp = parts[1].trim();
                    String[] imagenameTemp = temp.split("\\?");
                    String imagename = imagenameTemp[0].trim();
                    String finalimagename=imagename.replaceAll("%20"," ");
                    String[] fileexttemp = finalimagename.split("\\.");
                    String fileext = fileexttemp[1].trim();
                    String newfilename = filename+"."+fileext;
                    downloadFile(finalimagename, newfilename);
                    }

            });
            whatsappmyFab.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    View parentRow = (View) v.getParent();
                    RecyclerView recyclerView = (RecyclerView) parentRow.getParent();
                    String strexttemp = null;
                    int pos = getAdapterPosition();
                    ImageUpload mylistname = listImage.get(pos);
                    final String filename = mylistname.getName();
                    String imageurl = mylistname.getUrl();
                    String[] parts = imageurl.split("\\%2F");
                    String temp = parts[1].trim();
                    String[] imagenameTemp = temp.split("\\?");
                    String imagename = imagenameTemp[0].trim();
                    String finalimagename = imagename.replaceAll("%20", " ");
                    String[] fileexttemp = finalimagename.split("\\.");
                    String fileext = fileexttemp[1].trim();
                    String newfilename = filename + "." + fileext;
                    Sharewhatsapp(finalimagename, newfilename);
                }
        });

        }


    }

    public File storagefile() {



        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            if (PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Wishes");
            } else {
                requestPermission(context);
            }

        }


        return storageDir;
    }
    private void requestPermission(final Context context){
        if(ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(context, R.style.MyDialogTheme)
                    .setMessage("Do You Want To Give Permission?")
                    .setNegativeButton("No",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(context,"Permission Denied and Download Failed",Toast.LENGTH_LONG);
                        }
                    })
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
                        }
                    }).show();


        }else {
            ActivityCompat.requestPermissions((Activity) context,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_WRITE_EXTERNAL_STORAGE);
        }
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImageUpload mylist = listImage.get(position);
        Glide.with(context).load(mylist.getUrl()).into(holder.itemimage);
    }

        @Override
        public int getItemCount() {
            return listImage.size();
        }



    private void Sharewhatsapp(String fname, String outfname ) {
        StorageReference islandRef = mStorageRef.child(fname);
        File rootPath = new File(context.getExternalCacheDir(), "wishes");
        if(!rootPath.exists()) {
            rootPath.mkdirs();
        }

        final File localFile = new File(rootPath,outfname);
        islandRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setPackage("com.whatsapp");
                    i.setType("image/*");
                    Uri uri = FileProvider.getUriForFile(context, context.getString(R.string.file_provider_authority), localFile );
                    i.putExtra(Intent.EXTRA_STREAM, uri);
                    i.putExtra(Intent.EXTRA_TEXT,"https://play.google.com/store/apps/details?id=com.autodidact.developers.festivalwish");
                    context.startActivity(i);
                }
                catch (Exception e){
                    Toast.makeText(context,"Whatsapp is not installed in your device",Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("firebase ",";local tem file not created  created " +exception.toString());
            }
        });


    }
        private void ShareFile(String fname, String outfname ) {
            StorageReference islandRef = mStorageRef.child(fname);
            File rootPath = new File(context.getExternalCacheDir(), "wishes");
            if(!rootPath.exists()) {
                rootPath.mkdirs();
            }

            final File localFile = new File(rootPath,outfname);
            islandRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("image/*");
                    //                   Uri uri = Uri.fromFile(file);
                    Uri uri = FileProvider.getUriForFile(context, context.getString(R.string.file_provider_authority), localFile );
                    i.putExtra(Intent.EXTRA_STREAM, uri);
                    i.putExtra(Intent.EXTRA_TEXT,"https://play.google.com/store/apps/details?id=com.autodidact.developers.festivalwish");
                    context.startActivity(Intent.createChooser(i, "Share"));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.e("firebase ",";local tem file not created  created " +exception.toString());
                }
            });


        }
        private void downloadFile(String fname, String outfname) {
            StorageReference  islandRef = mStorageRef.child(fname);
            File rootPath = new File(String.valueOf(storagefile()));
            if (rootPath==null){
                return;
            }
            if(!rootPath.exists()) {
                rootPath.mkdirs();
            }

            final File localFile = new File(rootPath,outfname);
            islandRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(context, "Downloaded", Toast.LENGTH_LONG ).show();
                    refreshgallery(localFile);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(context, "Download Failed", Toast.LENGTH_LONG ).show();
//                    if (fileflag == 1){
//                        Toast.makeText(context, "Permission Enabled, Please Click Again To Download", Toast.LENGTH_LONG ).show();
//                        fileflag = 0;
//                    }

                }

            });
        }
        public void refreshgallery(File file){
            Intent downintent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            downintent.setData(Uri.fromFile(file));
            context.sendBroadcast(downintent);
        }

}
