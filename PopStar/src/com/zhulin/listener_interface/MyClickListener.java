package com.zhulin.listener_interface;

import com.zhulin.star.Star;

public interface MyClickListener {

	/**
	 * @author zew
	 * @exception 第一次点击选中星星
	 * @return void
	 * @param 
	 */
	void onSelectClick(Star star);

	// **第二次点击，消灭星星*/
	void onPopClick(Star star);
}
