package com.onlyou.jmeter.bpc;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import com.onlyou.bpc.pub.vo.ChangeBillStatusRequestVO;
import com.onlyou.bpc.pub.vo.ChangeBillStatusResponseVO;

/**
 * 业务系统通知票据中心状态
 * @author HQH
 *
 */
public class BizSysNotifyBpcServiceTest extends BaseServiceTest {
	
	/**
	 * 业务系统来源:zyd/es/malay/bpo
	 */
	private String source;
	
	/**
	 * 票据编号
	 */
	private String billCode;
	
	/**
	 * 额外信息
	 */
	private String info;
	
	/**
	 * 票据状态
	 * <p>
	 * IN_DB_SUCCESS - 入库成功（40）  IN_DB_ERROR - 入库失败（41） DELETED - 业务删除（90）
	 * </p>
	 */
	private String status;
	
	@Override
    public Arguments getDefaultParameters() {
		Arguments params = new Arguments();
        params.addArgument("source", source);
        params.addArgument("billCode", billCode);
        params.addArgument("info", info);
        params.addArgument("status", status);
        return params;
	}

	public SampleResult runTest(JavaSamplerContext context) {
		SampleResult sampleResult = new SampleResult();
		sampleResult.setSampleLabel("业务系统通知票据中心状态");
        sampleResult.sampleStart(); // 开始统计响应时间标记
        startTime = System.currentTimeMillis();
        
        //获取jmeter输入的参数值
        getParamValues(context);
        
        ChangeBillStatusRequestVO request = new ChangeBillStatusRequestVO();
        request.setSource(getSource(source));
        request.setBillCode(billCode);
        request.setInfo(info);
        request.setStatus(getStatus(status));
        
        try {
			ChangeBillStatusResponseVO response = super.bpcPublicService.changeBillStatus(request);
			if(response != null) {
				logger.info("更新结果状态：" + response.getCode().getCode() + ", " + response.getCode().getName());
				sampleResult.setSuccessful(true);
                sampleResult.setResponseCodeOK();
                sampleResult.setResponseData("更新结果状态：" + response.getCode().getName(), null);
                sampleResult.setDataType(SampleResult.TEXT);
			} else {
				sampleResult.setSuccessful(false);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			sampleResult.setSuccessful(false);
			sampleResult.setResponseData(e.getMessage(), null);
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
    	billCode = context.getParameter("billCode", billCode);
    	info = context.getParameter("info", info);
    	status = context.getParameter("status", status);
    	source = context.getParameter("source", source);
    }

	@Override
	protected long getStartTime() {
		return startTime;
	}

}
