package com.quantumcoinwallet.app.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.quantumcoinwallet.app.R;
import com.quantumcoinwallet.app.api.read.model.AccountTransactionSummary;
import com.quantumcoinwallet.app.utils.GlobalMethods;
import com.quantumcoinwallet.app.viewmodel.KeyViewModel;

import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

public class AccountTransactionAdapter extends
        Adapter<AccountTransactionAdapter.DataObjectHolder> {

    private static final String TAG = "AccountTransactionAdapter";

    public List<AccountTransactionSummary> accountTransactionSummaries;

    public static String walletAddress;

    private Context context;

    class DataObjectHolder extends ViewHolder {

        ImageView imageViewInOut;

        TextView textViewTransHash;
        TextView textViewDate;

        TextView textViewFrom;
        TextView textViewTo;

        TextView textViewQuantity;

        public DataObjectHolder(View itemView) {
            super(itemView);
            try{
                this.imageViewInOut = (ImageView) itemView.findViewById(R.id.imageView_account_transactions_adapter_in_out);
                this.textViewQuantity = (TextView) itemView.findViewById(R.id.textView_account_transactions_adapter_quantity);
                this.textViewDate = (TextView) itemView.findViewById(R.id.textView_account_transactions_adapter_date);
                this.textViewFrom = (TextView) itemView.findViewById(R.id.textView_account_transactions_adapter_from);
                this.textViewTo = (TextView) itemView.findViewById(R.id.textView_account_transactions_adapter_to);
                this.textViewTransHash = (TextView) itemView.findViewById(R.id.textView_account_transactions_adapter_trans_hash);
            } catch(Exception ex){
                GlobalMethods.ExceptionError(context, TAG, ex);
            }
        }
    }

    public AccountTransactionAdapter(Context context,
                                     List<AccountTransactionSummary> accountTransactionSummaries,
                                     String walletAddress) {
        this.context = context;
        this.accountTransactionSummaries = accountTransactionSummaries;
        this.walletAddress = walletAddress;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new DataObjectHolder(LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.account_transactions_adapter, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, @SuppressLint("RecyclerView") final int position) {
        try {

            String value = ((AccountTransactionSummary) accountTransactionSummaries.get(position)).getValue().toString();
            String createDate = ((AccountTransactionSummary) accountTransactionSummaries.get(position)).getCreatedAt().toString();
            String from =  ((AccountTransactionSummary) accountTransactionSummaries.get(position)).getFrom().toString();
            String to = ((AccountTransactionSummary) accountTransactionSummaries.get(position)).getTo().toString();
            String hash = ((AccountTransactionSummary) accountTransactionSummaries.get(position)).getHash().toString();

            int inOut = 1;

            if(walletAddress.toLowerCase().equals(from.toLowerCase())){
                inOut = 2;
            }

            switch (inOut) {
                case 1:
                    holder.imageViewInOut.setImageResource(R.drawable.arrow_down_circle_outline);
                    break;
                case 2:
                    holder.imageViewInOut.setImageResource(R.drawable.arrow_up_circle_outline);
                    break;
            }

            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            sd.setTimeZone(TimeZone.getTimeZone("GMT"));
            String formattedDateString = OffsetDateTime.parse(createDate).format(DateTimeFormatter.ofPattern("E, dd MMM yyyy HH:mm:ss"));

            BigInteger valueBigInteger = new BigInteger(value.replace("0x",""), 16);
            String wei = valueBigInteger.toString(10);
            KeyViewModel keyViewModel = new KeyViewModel();
            String quantity = (String) keyViewModel.getWeiToDogeProtocol(wei);

            holder.textViewTransHash.setText(hash.substring(0,7));
            holder.textViewDate.setText(formattedDateString + " GMT");
            holder.textViewFrom.setText(from.substring(0,7));
            holder.textViewTo.setText(to.substring(0,7));
            holder.textViewQuantity.setText(quantity);

            holder.textViewTransHash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse(GlobalMethods.BLOCK_EXPLORER_URL + GlobalMethods.BLOCK_EXPLORER_TX_HASH_URL.replace("{txhash}", hash)))
                    );
                }
            });

            holder.textViewFrom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse(GlobalMethods.BLOCK_EXPLORER_URL + GlobalMethods.BLOCK_EXPLORER_ACCOUNT_TRANSACTION_URL.replace("{address}", from)))
                    );
                }
            });

            holder.textViewTo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse(GlobalMethods.BLOCK_EXPLORER_URL + GlobalMethods.BLOCK_EXPLORER_ACCOUNT_TRANSACTION_URL.replace("{address}", to)))
                    );
                }
            });

        }catch(Exception ex){
            GlobalMethods.ExceptionError(context, TAG, ex);
        }
    }

    @Override
    public int getItemCount() {
        return this.accountTransactionSummaries == null ? 0 : this.accountTransactionSummaries.size();
    }
}
