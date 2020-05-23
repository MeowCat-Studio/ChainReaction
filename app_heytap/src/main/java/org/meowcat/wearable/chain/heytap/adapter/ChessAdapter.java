package org.meowcat.wearable.chain.heytap.adapter;

import android.animation.ValueAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.meowcat.wearable.chain.heytap.MeowCatApplication;
import org.meowcat.wearable.chain.heytap.R;
import org.meowcat.wearable.chain.heytap.game.Control;
import org.meowcat.wearable.chain.heytap.model.ChessModel;
import org.meowcat.wearable.chain.heytap.util.ChessLinearLayout;

/**
 * Created by luern0313 on 2020/5/5.
 */
public class ChessAdapter extends RecyclerView.Adapter<ChessAdapter.ViewHolder> {
    private ChessModel chessModel;
    private Control control;

    private int animSpeed;

    public ChessAdapter(ChessModel chessModel, Control control, int animSpeed) {
        this.chessModel = chessModel;
        this.control = control;
        this.animSpeed = animSpeed;
    }

    public void setAnimSpeed(int animSpeed) {
        this.animSpeed = animSpeed;
    }

    @NonNull
    @Override
    public ChessAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chess_piece, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ChessAdapter.ViewHolder holder, int position) {
        if (position == (chessModel.getChessSize() * chessModel.getChessSize() - chessModel.getChessSize() / 2 - 1))
            holder.grid_img.setColorFilter(MeowCatApplication.getContext().getResources().getColor(R.color.chess_blue));
        else if (position == chessModel.getChessSize() / 2)
            holder.grid_img.setColorFilter(MeowCatApplication.getContext().getResources().getColor(R.color.chess_red));
        else
            holder.grid_img.setColorFilter(MeowCatApplication.getContext().getResources().getColor(R.color.gray));

        if (chessModel.getChessChessPreview().get(position / chessModel.getChessSize()).get(position % chessModel.getChessSize()))
            holder.grid_lay2.setBackgroundResource(R.drawable.img_bg_chess_preview);
        else
            holder.grid_lay2.setBackgroundResource(0);
        if (position % 2 == 0)
            holder.grid_lay.setBackgroundColor(MeowCatApplication.getContext().getResources().getColor(R.color.chess_bg_yellow));
        else
            holder.grid_lay.setBackgroundColor(MeowCatApplication.getContext().getResources().getColor(R.color.chess_bg_white));

        float afterAngle = 90f * chessModel.getChessChess().get(position / chessModel.getChessSize()).get(position % chessModel.getChessSize());
        if (position == control.getX() * chessModel.getChessSize() + control.getY()) {
            float beforeAngle = afterAngle - 90f * control.getAngle();
            if (beforeAngle != afterAngle) {
                ValueAnimator anim = ValueAnimator.ofFloat(beforeAngle, afterAngle);
                anim.setDuration(animSpeed * 200 + 100);
                anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float currentValue = (float) animation.getAnimatedValue();
                        holder.grid_img.setRotation(currentValue);
                    }
                });
                anim.start();
            }
            control.setX(-1);
            control.setY(-1);
        } else
            holder.grid_img.setRotation(afterAngle);
    }

    @Override
    public int getItemCount() {
        return chessModel.getChessSize() * chessModel.getChessSize();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ChessLinearLayout grid_lay;
        private LinearLayout grid_lay2;
        private ImageView grid_img;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            grid_lay = itemView.findViewById(R.id.item_chess_lay);
            grid_lay2 = itemView.findViewById(R.id.item_chess_lay2);
            grid_img = itemView.findViewById(R.id.item_chess_chess);
            grid_lay.setLines(chessModel.getChessSize());
        }
    }
}
