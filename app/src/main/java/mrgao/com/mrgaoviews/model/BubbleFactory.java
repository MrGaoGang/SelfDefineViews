package mrgao.com.mrgaoviews.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import mrgao.com.mrgaoviews.views.ChargeBubbleView;

/**
 * Created by mr.gao on 2018/1/9.
 * Package:    mrgao.com.mrgaoviews.model
 * Create Date:2018/1/9
 * Project Name:SelfDefineViews
 * Description:
 */

public class BubbleFactory {
    private Random mRandom = new Random();
    private int mBubbleFloatTime = 3000;//气泡飘动一周的时间
    private long mAddTime;

    private Bubble getBubble(int maxRadius) {
        Bubble bubble = new Bubble();

        int type = mRandom.nextInt(3);

        switch (type) {
            case 0:
                bubble.setType(ChargeBubbleView.BubbleStartType.MIDDLE);
                break;
            case 1:
                bubble.setType(ChargeBubbleView.BubbleStartType.LITTLE);

                break;
            case 2:
                bubble.setType(ChargeBubbleView.BubbleStartType.BIG);

                break;
        }


        mAddTime = mAddTime + mRandom.nextInt(mBubbleFloatTime*2 );
        bubble.setTime(mAddTime + System.currentTimeMillis());

        bubble.setRadius(mRandom.nextInt(maxRadius - 15) + 15);//确保至少有15的半径

        return bubble;
    }


    /**
     * 产生气泡
     *
     * @param bubbleSize
     * @param bubbleMaxRadius
     * @return
     */
    public List<Bubble> getBubbles(int bubbleSize, int bubbleMaxRadius) {
        List<Bubble> list = new ArrayList<>();
        for (int i = 0; i < bubbleSize; i++) {
            list.add(getBubble(bubbleMaxRadius));
        }
        return list;
    }

    public BubbleFactory(int bubbleFloatTime) {
        mBubbleFloatTime = bubbleFloatTime;
    }
}
