package com.onlyou.jmeter.bpc;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import com.onlyou.bpc.bpo.exception.BpoException;
import com.onlyou.bpc.bpo.vo.UpdateBillStatusRequestVO;
import com.onlyou.bpc.bpo.vo.UpdateBillStatusResponseVO;
import com.onlyou.bpc.external.enumerator.ResponseEnum;

/**
 * 物理件通知票据中心处理票据状态
 * <p>
 * 物理件通知票据中心票据状态，票据中心需通知到业务系统（目前只有票据中心理票完成后将20录入中状态传给票据中心时用到）
 * </p>
 * @author HQH
 *
 */
public class BpoNotifyBpcServiceTest extends BaseServiceTest {
	
	/**
	 * 通知类型：packet=1; bill=2
	 */
	private String type = "1";
	
	/**
	 * 票据编号
	 */
	private String billCode = "X00000000062";
	
	/**
	 * 包裹编号
	 */
	private String packetCode = "V456000089";
	
	/**
	 * 状态
	 */
	private String status = "10";
	
	@Override
    public Arguments getDefaultParameters() {
		Arguments params = new Arguments();
        params.addArgument("type", type);
        params.addArgument("billCode", billCode);
        params.addArgument("packetCode", packetCode);
        params.addArgument("status", status);
        return params;
	}

	public SampleResult runTest(JavaSamplerContext context) {
		SampleResult sampleResult = new SampleResult();
		sampleResult.setSampleLabel("物理件通知票据中心处理票据状态");
        sampleResult.sampleStart(); // 开始统计响应时间标记
        startTime = System.currentTimeMillis();
        
        //获取jmeter输入的参数值
        getParamValues(context);
        
        UpdateBillStatusRequestVO request = new UpdateBillStatusRequestVO();
        request.setType(getType(type));
        request.setBillCode(billCode);
        request.setPacketCode(packetCode);
        request.setStatus(getStatus(status));
        
        try {
			UpdateBillStatusResponseVO response = bpoBillService.updateBillStatus(request);
			if(response != null) {
				logger.info("更新票据状态：" + response.getCode().getCode() + ", 返回信息:" + response.getCode().getName());
				if(ResponseEnum.SUCCESS.getCode().equals(response.getCode().getCode())) {
					sampleResult.setSuccessful(true);
	                sampleResult.setResponseCodeOK();
	                sampleResult.setResponseData("更新票据状态：" + response.getCode().getCode() + ", 返回信息:" + response.getCode().getName() + ",success:" + true, null);
	                sampleResult.setDataType(SampleResult.TEXT);
				} else {
					sampleResult.setSuccessful(false);
				}
			} else {
				logger.warn("更新失败！");
				sampleResult.setSuccessful(false);
				sampleResult.setResponseData("更新失败！" + ",success:" + false, null);
                sampleResult.setDataType(SampleResult.TEXT);
			}
		} catch (BpoException e) {
			logger.error(e.getMessage(), e);
			sampleResult.setSuccessful(false);
			sampleResult.setResponseData(e.getMessage() + ",success:" + false, null);
            sampleResult.setDataType(SampleResult.TEXT);
		} finally {
			sampleResult.sampleEnd(); // 结束统计响应时间标记
		}
        
		return sampleResult;
	}
	
	/**
     * 获取jmeter输入的参数值
     */
    private void getParamValues(JavaSamplerContext context) {
    	type = context.getParameter("type", type);
    	billCode = context.getParameter("billCode", billCode);
    	packetCode = context.getParameter("packetCode", packetCode);
    	status = context.getParameter("status", status);
    }

	@Override
	protected long getStartTime() {
		return startTime;
	}
	
	public static void main(String[] args) {
		BpoNotifyBpcServiceTest service = new BpoNotifyBpcServiceTest();
    	JavaSamplerContext context = new JavaSamplerContext(service.getDefaultParameters());
    	service.setupTest(context);
    	service.runTest(context);
    	service.teardownTest(context);
        //System.exit(0);
	}

}
