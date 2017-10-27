package com.onlyou.jmeter.bpc;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import com.onlyou.bpc.bpo.vo.SaveBillDataRequestVO;
import com.onlyou.bpc.bpo.vo.SaveBillDataResponseVO;

import java.util.HashMap;
import java.util.Map;

/**
 * 票据数据入库
 * <p>
 * 录入中心完成录入后，发送录入数据给票据中心
 * </p>
 * @author HQH
 *
 */
public class BpoSendData2BpcServiceTest extends BaseServiceTest {
	
	/**
	 * 票据编号
	 */
	private String billCode;
	
	/**
	 * 是否问题件
	 */
	private boolean broken;
	
	/**
	 * 问题件的原因
	 */
	private String brokenInfo;
	
	@Override
    public Arguments getDefaultParameters() {
		Arguments params = new Arguments();
        params.addArgument("billCode", billCode);
        params.addArgument("broken", broken + "");
        params.addArgument("brokenInfo", brokenInfo);

        params.addArgument("BarCode", "X51000101549");
        params.addArgument("CustCode", "06a60132f5884633a22953b41f6476d1");
        params.addArgument("BranchCode", "1004");
        params.addArgument("ImgURL", "/usuatdzbfftp1/1508293213857/201703/V510100684/X51000101549.jpg");
        params.addArgument("ThumbnailURL", "/usuatdzbfftp1/1508293213857/201703/V510100684/X51000101549_T.jpg");
        params.addArgument("BillType", "厦门市地方税务局通用机打发票");
        params.addArgument("BillTypeCode", "");
        params.addArgument("SequenceNO", "0006");
        params.addArgument("BillBatch", "V510100684_B");
        params.addArgument("PageNO", "");
        params.addArgument("TemplateGroupName", "国地税（增值税专用发票明细表）");
        params.addArgument("TemplateGroupNO", "KJDZ0004");
//        params.addArgument("Details");
        params.addArgument("ModeCode", "");
        return params;
	}

	public SampleResult runTest(JavaSamplerContext context) {
		SampleResult sampleResult = new SampleResult();
		sampleResult.setSampleLabel("票据数据入库");
        sampleResult.sampleStart(); // 开始统计响应时间标记
        startTime = System.currentTimeMillis();
        
        //获取jmeter输入的参数值
        getParamValues(context);
        
        SaveBillDataRequestVO request = new SaveBillDataRequestVO();
        request.setBillCode(billCode);
        request.setBroken(broken);
        request.setBrokenInfo(brokenInfo);
        request.setData(getDataMap(context));
        
        try {
        	SaveBillDataResponseVO response = super.bpoBillService.saveBillData(request);
        	if(response != null) {
        		logger.info("保存状态：" + response.getCode().getCode() + ", " + response.getCode().getName());
        		sampleResult.setSuccessful(true);
                sampleResult.setResponseCodeOK();
                sampleResult.setResponseData("保存状态：" + response.getCode().getName(), null);
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
    	String brokenStr = context.getParameter("broken", broken + "");
    	broken = Boolean.valueOf(brokenStr);
    	brokenInfo = context.getParameter("brokenInfo", brokenInfo);
    }

	@Override
	protected long getStartTime() {
		return startTime;
	}

	public Map<String,Object> getDataMap(JavaSamplerContext context) {
    	Map<String, Object> dataMap = new HashMap<String, Object>();

		dataMap.put("BarCode", context.getParameter("BarCode", "X51000101549"));
		dataMap.put("CustCode", context.getParameter("CustCode", "06a60132f5884633a22953b41f6476d1"));
		dataMap.put("BranchCode", context.getParameter("BranchCode", "1004"));
		dataMap.put("ImgURL", context.getParameter("ImgURL", "/usuatdzbfftp1/1508293213857/201703/V510100684/X51000101549.jpg"));
		dataMap.put("ThumbnailURL", context.getParameter("ThumbnailURL", "/usuatdzbfftp1/1508293213857/201703/V510100684/X51000101549_T.jpg"));
		dataMap.put("BillType", context.getParameter("BillType", "厦门市地方税务局通用机打发票"));
		dataMap.put("BillTypeCode", context.getParameter("BillTypeCode", ""));
		dataMap.put("SequenceNO", context.getParameter("SequenceNO", "0006"));
		dataMap.put("BillBatch", context.getParameter("BillBatch", "V510100684_B"));
		dataMap.put("PageNO", context.getParameter("PageNO", ""));
		dataMap.put("TemplateGroupName", context.getParameter("TemplateGroupName", "国地税（增值税专用发票明细表）"));
		dataMap.put("TemplateGroupNO", context.getParameter("TemplateGroupNO", "KJDZ0004"));
		dataMap.put("Details", context.getParameter("Details", null));
		dataMap.put("ModeCode", context.getParameter("ModeCode", ""));

		return dataMap;
	}
}
