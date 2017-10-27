package com.onlyou.jmeter.bpc;

import com.onlyou.bpc.bpo.vo.SaveBillDataRequestVO;
import com.onlyou.bpc.bpo.vo.SaveBillDataResponseVO;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 银行对账单票据数据入库
 * <p>
 * 录入中心完成录入后，发送录入数据给票据中心
 * </p>
 * @author HQH
 *
 */
public class BpoSendData2BpcServiceTest2 extends BaseServiceTest {
	
	/**
	 * 票据编号
	 */
	private String billCode;
	
	private String custCode;

	private String sequenceNO;
	
	@Override
    public Arguments getDefaultParameters() {
		Arguments params = new Arguments();
        params.addArgument("billCode", billCode);
        params.addArgument("CustCode", "42be9b8833db479a9acb988a9f70c127");
        params.addArgument("SequenceNO", "0090");

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
        request.setBroken(false);
        request.setBrokenInfo(null);
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
		custCode = context.getParameter("CustCode", custCode);
		sequenceNO = context.getParameter("SequenceNO", sequenceNO);
    }

	@Override
	protected long getStartTime() {
		return startTime;
	}

	public Map<String,Object> getDataMap(JavaSamplerContext context) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		Map<String, Object> bankMap = new HashMap<String, Object>();
		Map<String, Object> baseInfoMap = new HashMap<String, Object>();
		List<Map<String, Object>> details = new ArrayList<Map<String, Object>>();
		Map<String, Object> d1 = new HashMap<String, Object>();
		Map<String, Object> d2 = new HashMap<String, Object>();

		baseInfoMap.put("BarCode", billCode);
		baseInfoMap.put("CustCode", custCode);
		baseInfoMap.put("BranchCode", "1004");
		baseInfoMap.put("ImgURL", "/usuatdzbfftp1/1508762857611/201808/V510101403/X51000113614.jpg");
		baseInfoMap.put("ThumbnailURL", "/usuatdzbfftp1/1508762857611/201808/V510101403/X51000113614_T.jpg");
		baseInfoMap.put("BillType", "中国建设银行股份有限公司活期存款明细账");
		baseInfoMap.put("BillTypeCode", "");
		baseInfoMap.put("SequenceNO", sequenceNO);
		baseInfoMap.put("FeeID", "");
		baseInfoMap.put("FeeCode", "");
		baseInfoMap.put("BillBatch", "");
		baseInfoMap.put("PageNO", "10");
		baseInfoMap.put("LastEndingBalance", "0");
		baseInfoMap.put("EndingBalance", "");
		baseInfoMap.put("BankCode", "建设银行");
		baseInfoMap.put("AccountName", "瑞英网络技术");
		baseInfoMap.put("AccountNumber", "35001656240052501695");
		baseInfoMap.put("BranchName", "建设银行");
		baseInfoMap.put("Currency", "人民币");
		baseInfoMap.put("TemplateGroupName", "银行对账单");
		baseInfoMap.put("TemplateGroupNO", "KJDZ0003");

		d1.put("Unit", "财库联网集中户");
		d1.put("Summary", "财税库行批量代扣");
		d1.put("Date", "20170602");
		d1.put("DebitAmount", "97.32");
		d1.put("CreditAmount", "0");
		d1.put("Balance", "58125.66");

		d2.put("Unit", "财库联网集中户");
		d2.put("Summary", "TIPS3001实时扣税");
		d2.put("Date", "20170603");
		d2.put("DebitAmount", "4166.15");
		d2.put("CreditAmount", "0");
		d2.put("Balance", "53959.51");

		details.add(d1);
		details.add(d2);

		bankMap.put("BaseInfo", baseInfoMap);
		bankMap.put("Details", details);
		bankMap.put("ModeCode", "");

		dataMap.put("BankStatement", bankMap);

		return dataMap;
	}
}
