package am.newway.aca.util;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.IntRange;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewMargin extends RecyclerView.ItemDecoration
{
    private int margin;

    public RecyclerViewMargin ( @IntRange(from=0)int margin ) {
        this.margin = margin;

    }

    @Override
    public void getItemOffsets( Rect outRect, View view,
                                RecyclerView parent, RecyclerView.State state) {

//        int position = parent.getChildLayoutPosition(view);
//        int nWidth = parent.getWidth();
//        int childWidth = view.getLayoutParams().width;
//
//        GridLayoutManager layoutManager = ( GridLayoutManager ) parent.getLayoutManager();
//        int nSpan = 1;
//        if(layoutManager != null)
//            nSpan = layoutManager.getSpanCount();
//
//        int nn = nWidth - (childWidth * nSpan);
//
//        Log.e( "Divider ##" , "getItemOffsets: " + nn  );
//
//        int nCoorrect = (margin + outRect.left) / 2;

        outRect.left = margin;
        outRect.top = margin;
        outRect.right = 0;
        outRect.bottom = 0;

        //Log.e( "Divider ##" , "getItemOffsets: " + outRect.right  );

//        int nItemCount = parent.getAdapter() == null ? 0 : parent.getAdapter().getItemCount();
//        if(1 <= nItemCount && position == (nItemCount - 1)){
//            outRect.bottom = margin*8;
//        }
    }
}
