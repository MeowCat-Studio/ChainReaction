package org.meowcat.wearable.chain.heytap.game;

import java.util.Random;

/**
 * 被 luern0313 创建于 2020/5/5.
 */
public class Ai
{
    private int forecast;
    private Control control;
    private int chessSize;

    public Ai(int level, Control control, int chessSize)
    {
        switch (level)
        {
            case 0:
                forecast = 0;
                break;
            case 1:
                forecast = 2;
                break;
            case 2:
                forecast = 8;
                break;
            case 3:
                forecast = 30;
                break;
        }
        this.control = control;
        this.chessSize = chessSize;
    }

    public int next()
    {
        int[] select = new int[]{0, 0};
        control.createTemp();
        control.setNext(new int[]{0, chessSize / 2});
        control.setAngle(-1);
        for(int i = 0; i < forecast; i++)
        {
            control.moveChess();
            if(control.getNext()[0] == -1)
            {
                if(control.getNext()[1] == 0) select[0] = 1;
                else if(control.getNext()[1] == 1) select[0] = -1;
                break;
            }
            else if(control.getNext()[0] == -2)
                break;
        }

        control.createTemp();
        control.setNext(new int[]{0, chessSize / 2});
        control.setAngle(1);
        for(int i = 0; i < forecast; i++)
        {
            control.moveChess();
            if(control.getNext()[0] == -1)
            {
                if(control.getNext()[1] == 0) select[1] = 1;
                else if(control.getNext()[1] == 1) select[1] = -1;
                break;
            }
            else if(control.getNext()[0] == -2)
                break;
        }

        if(select[0] > select[1])
            return -1;
        else if(select[0] < select[1])
            return 1;
        else if(select[0] == -1)
            return 0;
        else
            return new Random().nextInt(2) == 0 ? -1 : 1;
    }
}
