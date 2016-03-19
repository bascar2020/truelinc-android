package co.com.indibyte.truelink.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseImageView;

import java.util.ArrayList;
import java.util.List;

import co.com.indibyte.truelink.R;
import co.com.indibyte.truelink.TarjetaActivity;
import co.com.indibyte.truelink.model.Tarjetas;
import co.com.indibyte.truelink.util.utils;

public class CardsAdapterBuscador extends BaseAdapter implements Filterable {

    Context mContext;
    LayoutInflater inflater;
    private List<Tarjetas> tarjetasUser;// original data
    private List<Tarjetas> filteredData;


    public CardsAdapterBuscador(Context context, List<Tarjetas> tarjetasUser) {


        mContext = context;
        this.tarjetasUser = tarjetasUser;
        this.filteredData = tarjetasUser;
        //Log.d("DEB", ""+tarjetasUser.size());
        inflater = LayoutInflater.from(mContext);
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();
                final List<Tarjetas> list = tarjetasUser;

                if (charSequence!=null || charSequence.length() !=0 ) {
                    List<Tarjetas> listResult = new ArrayList<Tarjetas>();
                    for (Tarjetas t : list){
                        if (t.getNombre().toLowerCase().contains(charSequence.toString().toLowerCase())||t.getEmpresa().toLowerCase().contains(charSequence.toString().toLowerCase())|| utils.isTaginarray(charSequence.toString().toLowerCase(), t.getTags())){
                            listResult.add(t);
                        }
                    }
                    results.values = listResult;
                    results.count = listResult.size();
                    return results;
                }else{
                    results.values = list;
                    results.count = list.size();
                    return results;
                }

            }


            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredData = (List<Tarjetas>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder{
        TextView nombre;
        TextView empresa;
        ParseImageView foto;
        ImageView logo;
        TextView cargo;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return filteredData.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        final ViewHolder holder;
        if (view == null){
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.list_row, null);



            holder.logo = (ImageView) view.findViewById(R.id.logo);
            holder.foto = (ParseImageView) view.findViewById(R.id.foto);
            holder.empresa = (TextView) view.findViewById(R.id.empresa);
            holder.nombre = (TextView) view.findViewById(R.id.nombre);
            holder.cargo = (TextView) view.findViewById(R.id.tv_cargo);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        if (filteredData.get(position).getFoto()!=null){
            holder.foto.setParseFile(filteredData.get(position).getFoto());
            holder.foto.loadInBackground();
        }else{
            int imageResource = R.drawable.no_perfil;
            holder.foto.setImageResource(imageResource);
        }



        byte[] bitmapdata = new byte[0];
        try {
            if (filteredData.get(position).getLogo()!=null) {
                bitmapdata = filteredData.get(position).getLogo().getData();
                Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
                holder.logo.setImageBitmap(bitmap);
            }else{
                int imageResource = R.drawable.nologo;
                holder.logo.setImageResource(imageResource);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        holder.empresa.setText(capitalizeFirstLetter(filteredData.get(position).getEmpresa()));
        holder.nombre.setText(capitalizeFirstLetter(filteredData.get(position).getNombre()));
        holder.cargo.setText(capitalizeFirstLetter(filteredData.get(position).getCargo()));
//        Log.d("DEB", tarjetasUser.get(position).getLogo().toString());

        // Listen for ListView Item Click
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Bundle bundle = new Bundle();
                if (filteredData.get(position).getNombre()==null){bundle.putString("Nombre", "");}else{
                    bundle.putString("Nombre", capitalizeFirstLetter(filteredData.get(position).getNombre()));}
                if (filteredData.get(position).getEmpresa()==null){bundle.putString("Empresa", "");}else{bundle.putString("Empresa", capitalizeFirstLetter(filteredData.get(position).getEmpresa()));}
                if (filteredData.get(position).getCargo()==null){bundle.putString("Cargo", "");}else{bundle.putString("Cargo", filteredData.get(position).getCargo());}
                if (filteredData.get(position).getDireccion()==null){bundle.putString("Direccion", "");}else{bundle.putString("Direccion", filteredData.get(position).getDireccion());}
                if (filteredData.get(position).getTelefono()==null){bundle.putString("Telefono", "");}else{bundle.putString("Telefono", filteredData.get(position).getTelefono());}
                if (filteredData.get(position).getEmail()==null){bundle.putString("Email", "");}else{bundle.putString("Email", filteredData.get(position).getEmail());}
                if (filteredData.get(position).getCiudad()==null){bundle.putString("Ciudad", "");}else{bundle.putString("Ciudad", filteredData.get(position).getCiudad());}
                if (filteredData.get(position).getTwit()==null){bundle.putString("Twit", "");}else{bundle.putString("Twit", filteredData.get(position).getTwit());}
                bundle.putString("objecId",filteredData.get(position).getObjectID());
                bundle.putBoolean("follow",false);// esta variable representa que el usuario sigue esta tarjeta
                if (filteredData.get(position).getTwiter()==null){bundle.putString("twiter", "");}else{bundle.putString("twiter", filteredData.get(position).getTwiter());}
                if (filteredData.get(position).getFacebook()==null){bundle.putString("facebook", "");}else{bundle.putString("facebook", filteredData.get(position).getFacebook());}
                if (filteredData.get(position).getWWW() == null) {bundle.putString("www", "");} else {bundle.putString("www", filteredData.get(position).getWWW());}
                try {
                    if (filteredData.get(position).getFoto() == null){bundle.putByteArray("Foto",null);}else{bundle.putByteArray("Foto", filteredData.get(position).getFoto().getData());}
                    if (filteredData.get(position).getLogo()==null){bundle.putByteArray("LogoEmpresa",null);}else{bundle.putByteArray("LogoEmpresa", filteredData.get(position).getLogo().getData());}
                    if (filteredData.get(position).getQr()==null){bundle.putByteArray("Qr",null);}else{bundle.putByteArray("Qr", filteredData.get(position).getQr().getData());}
                } catch (ParseException e) {
                    e.printStackTrace();
                    Log.d("Error",e.getMessage());
                }
                // Send single item click data to SingleItemView Class



                Intent intent = new Intent(mContext, TarjetaActivity.class);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });

        return view;
    }

    public String capitalizeFirstLetter(String original) {

        if (original == null || original.length() == 0) {
            return original;
        }


        String resultado = "";
        for (String tmp:original.split(" ")) {
            resultado +=  tmp.substring(0, 1).toUpperCase() + tmp.substring(1) + " ";
        }

        return resultado.trim();
    }



}

