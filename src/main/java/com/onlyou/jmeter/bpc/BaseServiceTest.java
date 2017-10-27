package com.onlyou.jmeter.bpc;

import java.util.HashMap;
import java.util.Map;

import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.log.Logger;

import com.onlyou.bpc.bpo.service.IBpoBillService;
import com.onlyou.bpc.external.enumerator.SourceEnum;
import com.onlyou.bpc.external.enumerator.StatusEnum;
import com.onlyou.bpc.external.enumerator.StatusTypeEnum;
import com.onlyou.bpc.pub.service.IBpcPublicService;
import com.onlyou.jmeter.util.DubboContext;

/**
 * 基类
 * @author HQH
 *
 */
public abstract class BaseServiceTest extends AbstractJavaSamplerClient {
	
	protected Logger logger = super.getLogger();
	
	/**
	 * 影像文件
	 */
	protected static String imageFileName = "/image/jmetertestimage.jpg";
	
	/**
	 * OSS回调地址
	 */
	protected String ossCallbackUri = "http://172.16.11.74:8000/oss/upload/callback.json";
	
	/**
	 * 开始测试时间截
	 */
	protected long startTime = 0;
	
	/**
	 * 网点编号
	 */
	protected static String branchNum = "510"; //业务逻辑已经写死了，此处仅做非空参数处理。
	
	/**
	 * 票据中心服务 - 提供给BPO调用
	 */
	protected IBpoBillService bpoBillService;
	/**
	 * 票据中心服务 - 提供外部系统调用
	 */
	protected IBpcPublicService bpcPublicService;
	
	private static Map<String, SourceEnum> sourceMap = new HashMap<String, SourceEnum>();
	static {
		sourceMap.put("zyd", SourceEnum.ZYD);
		sourceMap.put("es", SourceEnum.ES);
		sourceMap.put("malay", SourceEnum.MALAY);
		sourceMap.put("bpo", SourceEnum.BPO);
	}
	
	private static Map<String, StatusTypeEnum> typeMap = new HashMap<String, StatusTypeEnum>();
	static {
		typeMap.put("1", StatusTypeEnum.PACKET);
		typeMap.put("2", StatusTypeEnum.BILL);
	}
	
	private static Map<String, StatusEnum> statusMap = new HashMap<String, StatusEnum>();
	static {
		statusMap.put("00", StatusEnum.NEW);
		statusMap.put("10", StatusEnum.DISTINGUISHING);
		statusMap.put("20", StatusEnum.TYPING);
		statusMap.put("30", StatusEnum.TYPED);
		statusMap.put("40", StatusEnum.IN_DB_SUCCESS);
		statusMap.put("41", StatusEnum.IN_DB_ERROR);
		statusMap.put("90", StatusEnum.DELETED);
	}
	
	/**
	 * 每个线程测试前执行一次，做一些初始化工作；
	 */
	@Override
    public void setupTest(JavaSamplerContext context) {
		DubboContext.getInstance().initApplicationContext();
		bpoBillService = DubboContext.getInstance().getBean(IBpoBillService.class);
		bpcPublicService = DubboContext.getInstance().getBean(IBpcPublicService.class);
	}
	
	/**
	 * 测试结束时调用
	 */
	@Override
    public void teardownTest(JavaSamplerContext context) {
		logger.info("### cost time: " + (System.currentTimeMillis() - this.getStartTime()) + "ms ###");
    }
	
	/**
	 * 来源转换
	 * @param source
	 * @return
	 */
	protected SourceEnum getSource(String source) {
		return sourceMap.get(source);
	}
	
	/**
	 * 通知类型转换
	 * @param type
	 * @return
	 */
	protected StatusTypeEnum getType(String type) {
		return typeMap.get(type);
	}
	
	/**
	 * 状态转换
	 * @param status
	 * @return
	 */
	protected StatusEnum getStatus(String status) {
		return statusMap.get(status);
	}
	
	/**
	 * 开始执行时间截
	 * @return
	 */
	protected abstract long getStartTime();

}
