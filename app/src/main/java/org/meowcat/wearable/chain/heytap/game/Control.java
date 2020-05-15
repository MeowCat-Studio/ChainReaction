package org.meowcat.wearable.chain.heytap.game;

import org.meowcat.wearable.chain.heytap.model.ChessModel;
import org.meowcat.wearable.chain.heytap.model.AchievementModel;

import java.util.ArrayList;

import lombok.Data;

/**
 * 被 luern0313 创建于 2020/5/5.
 */
@Data
public class Control
{
    private ChessModel chessModel;
    private AchievementModel achievementModel;
    private boolean recordAchievement;
    private int tempX = -1;
    private int tempY = -1;
    private int x = -1;
    private int y = -1;
    private int angle;
    private int[] next;

    private ArrayList<ArrayList<Integer>> tempChess;
    private int tempMove = 0;

    public Control(final ChessModel chessModel, boolean recordAchievement)
    {
        this.chessModel = chessModel;
        this.tempChess = new ArrayList<ArrayList<Integer>>()
        {{
            for(int i = 0; i < chessModel.getChessSize(); i++)
                add(new ArrayList<Integer>());
        }};
        achievementModel = AchievementModel.getInstance();
        this.recordAchievement = recordAchievement;
        createTemp();
    }

    public void moveChess()
    {
        tempX = next[0];
        tempY = next[1];
        int a = (tempChess.get(tempX).get(tempY) + angle) % 4;
        if(a < 0) a = 4 + a;
        tempMove++;

        tempChess.get(tempX).set(tempY, a);
        if(a == 0 && tempY > 0)
            next = new int[]{tempX, tempY - 1};
        else if(a == 1 && tempX > 0)
            next = new int[]{tempX - 1, tempY};
        else if(a == 2 && tempY < chessModel.getChessSize() - 1)
            next = new int[]{tempX, tempY + 1};
        else if(a == 3 && tempX < chessModel.getChessSize() - 1)
            next = new int[]{tempX + 1, tempY};
        else if(a == 3 && tempX == chessModel.getChessSize() - 1 && tempY == chessModel.getChessSize() / 2)
            next = new int[]{-1, 0};
        else if(a == 1 && tempX == 0 && tempY == chessModel.getChessSize() / 2)
            next = new int[]{-1, 1};
        else
            next = new int[]{-2, -2};
    }

    public void preview(int player, int angle)
    {
        createTemp();
        chessModel.resetPreview();
        this.angle = angle;
        if(player == 0)
            next = new int[]{chessModel.getChessSize() - 1, chessModel.getChessSize() / 2};
        else
            next = new int[]{0, chessModel.getChessSize() / 2};
        while(next[0] >= 0)
        {
            chessModel.getChessChessPreview().get(next[0]).set(next[1], true);
            moveChess();
        }
    }

    public void apply()
    {
        for(int i = 0; i < tempChess.size(); i++)
        {
            chessModel.getChessChess().get(i).clear();
            for(int j = 0; j < tempChess.get(i).size(); j++)
                chessModel.getChessChess().get(i).add(tempChess.get(i).get(j));
        }
        if(recordAchievement)
            achievementModel.recordAchievement("旋转过箭头", tempMove);
        tempMove = 0;
        x = tempX;
        y = tempY;
    }

    public void createTemp()
    {
        for(int i = 0; i < chessModel.getChessChess().size(); i++)
        {
            tempChess.get(i).clear();
            for(int j = 0; j <chessModel.getChessChess().size(); j++)
                tempChess.get(i).add(chessModel.getChessChess().get(i).get(j));
        }
        tempMove = 0;
    }
}
