package yagmurdan.sonra.toprakkokusu.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import yagmurdan.sonra.toprakkokusu.Model.CampingArea;
import yagmurdan.sonra.toprakkokusu.R;

public class CampingAreaAdapter extends RecyclerView.Adapter<CampingAreaAdapter.ViewHolder> {

    public Context mContext;
    public List<CampingArea> mCampingArea;

    private OnCampingAreaListener mOnCampingAreaListener;


    public CampingAreaAdapter(Context mContext, List<CampingArea> mCampingArea, OnCampingAreaListener onCampingAreaListener) {
        this.mContext = mContext;
        this.mCampingArea = mCampingArea;
        this.mOnCampingAreaListener = onCampingAreaListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView CampingAreaImage;
        public TextView CampingAreaIsim, CampingAreaLokasyon;
        OnCampingAreaListener onCampingAreaListener;

        public ViewHolder(@NonNull View itemView, OnCampingAreaListener onCampingAreaListener) {
            super(itemView);

            CampingAreaImage = itemView.findViewById(R.id.CampingAreaItemImageView);
            CampingAreaIsim = itemView.findViewById(R.id.CampingAreaItemTextViewKampAdi);
            CampingAreaLokasyon = itemView.findViewById(R.id.CampingAreaItemTextViewLokasyon);

            this.onCampingAreaListener = onCampingAreaListener;

            itemView.setOnClickListener(this);
            CampingAreaImage.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            onCampingAreaListener.onCampingAreaClick(getAdapterPosition());
        }
    }

    //Bu method, adaptör oluşturulduğunda çağrılır ve ViewHolder'larımızı başlatmak için kullanılır.
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.camping_area_item, parent, false);
        return new ViewHolder(view,mOnCampingAreaListener);

    }


    //Verilerimizi ViewHolder'ımıza ileteceğimiz yer burasıdır.
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        CampingArea currentCampingArea = mCampingArea.get(position);

        holder.CampingAreaIsim.setText(currentCampingArea.getName());
        holder.CampingAreaLokasyon.setText(currentCampingArea.getLocation());
        Glide.with(mContext)
                .load(currentCampingArea.getGonderiResmi())
                .into(holder.CampingAreaImage);

    }

    @Override
    public int getItemCount() {
        return mCampingArea.size();
    }

    public interface OnCampingAreaListener{
        void onCampingAreaClick(int position);
    }
}
