package net.lanoda.app.android.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.BaseAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.lanoda.app.android.R;
import net.lanoda.app.android.models.contacts.ContactModel;

import java.io.InputStream;
import java.util.List;


/**
 * Created by isaac on 9/22/2016.
 */

public class ContactsArrayAdapter extends BaseAdapter {

    private Context context;
    private final List<ContactModel> contacts;

    public ContactsArrayAdapter(Context context, List<ContactModel> contacts) {
        this.context = context;
        this.contacts = contacts;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null) {

            gridView = new View(context);

            ContactModel currentContact = contacts.get(position);

            gridView = inflater.inflate(R.layout.adapter_contacts_grid_item, null);

            TextView contactName = (TextView) gridView.findViewById(R.id.contacts_grid_item_text);

            String displayName = currentContact.FirstName + " " + currentContact.LastName;
            contactName.setText(displayName);

            ImageView contactImage = (ImageView)
                    gridView.findViewById(R.id.contacts_grid_item_image);

            if (currentContact.ImageId != null && !currentContact.ImageId.equals("")
                    && !currentContact.ImageId.equals("null")) {
                String imageUrl = context.getString(R.string.api_root_url) +
                        "images/" + currentContact.ImageId;

                new DownloadImageTask(contactImage).execute(imageUrl);
            } else {
                contactImage.setImageResource(R.drawable.ic_person_black_24dp);
            }

        } else {
            gridView = (View) convertView;
        }

        return gridView;
    }

    @Override
    public int getCount() {
        return contacts.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urlDisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urlDisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}
