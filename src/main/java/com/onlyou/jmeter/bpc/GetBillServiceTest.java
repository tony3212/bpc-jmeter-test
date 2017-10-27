package com.onlyou.jmeter.bpc;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import com.onlyou.bpc.pub.vo.GetBillRequestVO;
import com.onlyou.bpc.pub.vo.GetBillResponseVO;

/**
 * 获取票据服务
 * @author HQH
 *
 */
public class GetBillServiceTest extends BaseServiceTest {
	
	/**
	 * 票据编号
	 */
	private String billCode;
	
	@Override
    public Arguments getDefaultParameters() {
		Arguments params = new Arguments();
        params.addArgument("billCode", billCode);
        return params;
	}

	public SampleResult runTest(JavaSamplerContext context) {
		SampleResult sampleResult = new SampleResult();
		sampleResult.setSampleLabel("获取票据");
        sampleResult.sampleStart(); // 开始统计响应时间标记
        startTime = System.currentTimeMillis();
        
        //获取jmeter输入的参数值
        getParamValues(context);
        
        GetBillRequestVO request = new GetBillRequestVO();
        request.setBillCode(billCode);
        
        try {
			GetBillResponseVO response = super.bpcPublicService.getBill(request);
			if(response != null) {
				logger.info("获取结果状态：" + response.getCode().getCode() + ", " + response.getCode().getName());
				logger.info("票据数据：" + ToStringBuilder.reflectionToString(response.getBill()));
				sampleResult.setSuccessful(true);
                sampleResult.setResponseCodeOK();
                sampleResult.setResponseData("票据数据：" + ToStringBuilder.reflectionToString(response.getBill()), null);
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
    }

	@Override
	protected long getStartTime() {
		return startTime;
	}
	
	public static void main(String[] args) {
		GetBillServiceTest service = new GetBillServiceTest();
		JavaSamplerContext context = new JavaSamplerContext(service.getDefaultParameters());
    	service.setupTest(context);
    	service.runTest(context);
    	service.teardownTest(context);
	}

}
