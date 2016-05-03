package com.suken.bridgedetection;

public class Constants {

	public static final String FIRST_LOGIN = "first_login";
	public static final String USER_INFO = "user_info";

	public static final String ERRORCODE = "errcode";

	public static final String ERRORMSG = "errmsg";

	public static final String SQLTAG = "ormlite";

	public static long LOCATION_RECORD_INTERVAL = 50 * 1000l;

	public static final String INTERVAL = "interval";
	public static final String GPS_SWITCH = "gps_switch";

	public static final int REQUEST_CODE_CAPTURE = 1;
	public static final int REQUEST_CODE_EDIT_IMG = 2;
	public static final int REQUEST_CODE_VIDEO = 3;

	public static final String[] qlformDetailItemTexts = new String[] { "缺损类型：", "缺损范围：", "保养措施意见：" };

	public static final String[] qlformDetailNames = new String[] { "翼墙", "锥坡、护坡", "桥台及基础", "桥墩及基础", "地基冲刷", "支座", "上部结构异常变形", "桥与路连接", "伸缩缝","桥面铺装", "人行道、缘石",
			"栏杆、护栏", "标志、标线", "排水设施", "照明系统", "桥面清洁", "调治结构物", "其他" };
	public static final String[] qlformDetailValues = new String[] { "完好或有无开裂、倾斜、滑移、沉降、风化剥落和异常变形等", "完好或有无塌陷、铺砌面有无缺损、勾缝脱落、灌木杂草丛生",
			"完好或桥台受车辆、漂浮物撞击而受损，基础是否受到冲刷损坏、外露、悬空、下沉及生物腐蚀", "完好或桥墩受车辆、漂浮物撞击而受损，基础是否受到基础冲刷损坏、钢筋外露、悬空、下沉及生物腐蚀", "完好或桥位区段河床冲淤变化情况",
			"完好或有/无明显缺陷、倾斜变形，活动支座缺损、是否灵活，位移量正常是否", "完好或有/无异常变形、开裂，异常的竖向振动异常、横向摆动异常，各部件病害情况和（手工填写", "完好或路桥连接是否顺畅", "完好或伸缩缝是否堵塞卡死，连接部件有无松动、连接脱落、局部破损",
			"完好或是否平整，有无裂缝、有坑槽、有积水、有沉陷、有波浪、有碎边；桥面是否剥落、渗漏，钢筋是否露筋、钢筋锈蚀，缝料是否老化损坏，桥头有无跳车。", "完好或有无撞坏、断裂、松动错位、缺件、剥落、锈蚀等。", "完好或有无撞坏、断裂、松动错位、缺件、剥落、锈蚀等。", "完好或缺损、模糊不清",
			"良好或泄水管堵塞、破损。", "完好或缺损", "整洁或有无杂物堆积，杂草蔓生。", "完好或有无塌陷、铺砌面有无缺损、勾缝脱落、灌木杂草丛生", "构件表面的涂装层是否完好，有无损坏、老化变色、开裂、起皮、剥落、锈迹，其他显而易见的损坏或病害" };
	public static final String[] qlformDetailEt2Blanks = new String[] { "提示：描述翼墙位置（上、下行方向，左右位置）和缺损程度（手工填写）", "提示：描述翼墙位置（上、下行方向，左右位置）和缺损程度（手工填写）",
			"提示：描述翼墙位置（上、下行方向，左右位置）和缺损程度（手工填写）", "提示：描述翼墙位置（上、下行方向，左右位置）和缺损程度（手工填写）", "提示：描述翼墙位置（上、下行方向，左右位置）和缺损程度（手工填写）", "提示：描述翼墙位置（上、下行方向，左右位置）和缺损程度（手工填写）",
			"提示：描述翼墙位置（上、下行方向，左右位置）和缺损程度（手工填写）", "注明缺损情况（手工填写）", "注明缺损情况（手工填写）", "注明缺损情况（手工填写）", "注明缺损情况（手工填写）", "注明缺损情况（手工填写）", "注明缺损情况（手工填写）", "注明缺损情况（手工填写）",
			"注明缺损情况（手工填写）", "注明杂物类型、大小，杂草面积部位。（手工填写）", "注明缺损情况（手工填写）", "注明缺损情况（手工填写）" };
	public static final String[] qlformDetailEt3Blanks = new String[] { "注明继续观察或修复时限和采取的交通管制措施及向上级报告情况（手工填写）", "注明继续观察或修复时限或向上级报告情况（手工填写）",
			"注明继续观察或修复时限或向上级报告情况（手工填写）", "注明继续观察或修复时限或向上级报告情况（手工填写）", "注明继续观察或修复时限或向上级报告情况（手工填写）", "注明继续观察或修复时限或向上级报告情况（手工填写）",
			"（手工填写）注明继续观察或修复时限和采取的交通管制措施及向上级报告情况或建议定期检查或特殊检查", "注明继续观察或修复时限或向上级报告情况（手工填写）", "注明继续观察或修复时限或向上级报告情况（手工填写）", "注明继续观察或修复时限或向上级报告情况（手工填写）",
			"注明继续观察或修复时限或向上级报告情况（手工填写）", "注明继续观察或修复时限或向上级报告情况（手工填写）", "注明继续观察或修复时限或向上级报告情况（手工填写）", "注明继续观察或修复时限或向上级报告情况（手工填写）", "注明继续观察或修复时限或向上级报告情况（手工填写）",
			"注明继续观察或修复时限或向上级报告情况（手工填写）", "注明继续观察或修复时限或向上级报告情况（手工填写）", "注明继续观察或修复时限或向上级报告情况（手工填写）" };

	public static final String[] hdformDetailItemTexts = new String[] { "情况描述：", "保养措施意见：", "备注：" };
	public static final String[] hdformDetailNames = new String[] { "进（水）口", "出（水）口", "沉砂井", "涵洞内部", "洞口周围", "涵附近填土", "涵洞结构", "其它" };
	public static final String[] hdformDetailValues = new String[] { "完好或进（水）口是否堵塞", "完好或出（水）口是否堵塞", "完好或有无淤积", "完好或有无淤塞、排水不畅、是否清洁、漏水。", "完好或是否有杂物堆积。",
			"路基填土是否稳定和完整", "完好或结构有裂缝等病害，有无异常变形，异常的竖向振动、横向摆动，各部件病害情况。", "通道性质参考桥梁填写" };
	public static final String[] hdformDetailEt2Blanks = new String[] { "注明清除时限和履行的报告情况", "注明清除时限和履行的报告情况", "注明清除时限和履行的报告情况", "注明清除时限和履行的报告情况",
			"注明清除时限和履行的报告情况", "出现渗水、缺口及时封塞填平", "注明清除时限和履行的报告情况", "注明清除时限和履行的报告情况" };
	public static final String[] hdformDetailEt3Blanks = new String[] { "说明其他需要说明的问题" };

	public static final String[] sdformDetailNames = new String[] { "洞口", "洞门", "衬砌", "路面", "检修道", "排水设施", "吊顶及各种预埋件", "标志标线轮廓标", "其他" };
	public static final String[] sdformDetailItemTexts = new String[] { "异常位置：", "缺损内容：", "异常描述：", "判定：", "保养措施意见：" };
	public static final String[] sdxcformDetailNames = new String[] { "洞口边仰坡", "洞门结构", "内部衬砌", "洞内路面积水", "洞内路面撒落物", "洞顶预埋件和悬吊件", "供配电设施", "照明设施", "通风设施",
			"消防设施", "监控和通信设施", "其他" };
	public static final String[] sdxcformDetailItemTexts = new String[] { "情况描述：", "处置意见：", "检查时间：" };

	public static final String[] qhxcformDetailItemTexts = new String[] { "方向：", "发现病害：", "损害位置：", "尺寸大小：", "病害原因：" };

	public static final String[] weatherStrs = new String[] { "晴", "多云", "阴", "雨", "雪", "雾霾", "冰雹" };

	public static final String STATUS_CHECK = "0";
	public static final String STATUS_UPDATE = "1";
	public static final String STATUS_AGAIN = "2";

}
