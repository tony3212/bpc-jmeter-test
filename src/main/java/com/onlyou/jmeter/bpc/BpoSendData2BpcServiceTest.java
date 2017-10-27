package com.onlyou.jmeter.bpc;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import com.onlyou.bpc.bpo.vo.SaveBillDataRequestVO;
import com.onlyou.bpc.bpo.vo.SaveBillDataResponseVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
	
	private String custCode;

	private String sequenceNO;
	
	@Override
    public Arguments getDefaultParameters() {
		Arguments params = new Arguments();
		params.addArgument("billCode", "X51000113529");
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
		Map<String, Object> invoiceMap = new HashMap<String, Object>();
		Map<String, Object> baseInfoMap = new HashMap<String, Object>();
		List<Map<String, Object>> details = new ArrayList<Map<String, Object>>();
		Map<String, Object> d1 = new HashMap<String, Object>();

		baseInfoMap.put("BarCode", billCode);
		baseInfoMap.put("CustCode", custCode);
		baseInfoMap.put("BranchCode", "1004");
		baseInfoMap.put("ImgURL", "/usuatdzbfftp1/1508762857611/201808/V510101403/X51000113529.jpg");
		baseInfoMap.put("ThumbnailURL", "/usuatdzbfftp1/1508762857611/201808/V510101403/X51000113529_T.jpg");
		baseInfoMap.put("SequenceNO", sequenceNO);
		baseInfoMap.put("BillType", "厦门市地方税务局通用机打发票");
		baseInfoMap.put("BillTypeCode", "");
		baseInfoMap.put("AccountingBillType", "");
		baseInfoMap.put("AccountingBillTypeCode", "");
		baseInfoMap.put("BillBatch", "");
		baseInfoMap.put("TemplateGroupName", "收入发票");
		baseInfoMap.put("TemplateGroupNO", "KJDZ0101");
		baseInfoMap.put("FeeID", "");
		baseInfoMap.put("FeeCode", "");
		baseInfoMap.put("BillNum", "1");
		baseInfoMap.put("InvoiceCode", "");
		baseInfoMap.put("InvoiceNO", "07386223");
		baseInfoMap.put("InvoiceCode2", "");
		baseInfoMap.put("InvoiceNO2", "");
		baseInfoMap.put("Sheet", "记账联");
		baseInfoMap.put("PayingCorp", "华为技术有限公司");
		baseInfoMap.put("PayingTaxCode", "");
		baseInfoMap.put("PayingAddressPhone", "");
		baseInfoMap.put("PayingAccountBank", "");
		baseInfoMap.put("DrawingCorp", "瑞英网络技术");
		baseInfoMap.put("DrawingTaxCode", "");
		baseInfoMap.put("DrawingAddressPhone", "");
		baseInfoMap.put("DrawingAccountBank", "");
		baseInfoMap.put("IssueDate", "20150301");
		baseInfoMap.put("CharYearMonth", "");
		baseInfoMap.put("IssueDateRes", "");
		baseInfoMap.put("Overdue", "");
		baseInfoMap.put("Currency", "");
		baseInfoMap.put("ExRate", "");
		baseInfoMap.put("NetInvoiceNO", "");
		baseInfoMap.put("SubTotalTax", "");
		baseInfoMap.put("TotalTax", "");
		baseInfoMap.put("SubTotalAmount", "");
		baseInfoMap.put("TotalAmount", "880");
		baseInfoMap.put("FCTotalAmount", "0");
		baseInfoMap.put("PenaltyAmount", "");
		baseInfoMap.put("Summary", "");
		baseInfoMap.put("Category", "");
		baseInfoMap.put("CategoryCode", "");
		baseInfoMap.put("DueDate", "");
		baseInfoMap.put("Month", "");
		baseInfoMap.put("Department", "");
		baseInfoMap.put("BasicWages", "");
		baseInfoMap.put("SocialInsurance", "");
		baseInfoMap.put("AccumulationFund", "");
		baseInfoMap.put("IndividualIncomeTax", "");
		baseInfoMap.put("RealWages", "");
		baseInfoMap.put("TaxRate", "");
		baseInfoMap.put("GoodsList", "否");
		baseInfoMap.put("Authenticity", "");
		baseInfoMap.put("Amount", "0");
		baseInfoMap.put("YesNo1", "");
		baseInfoMap.put("IssueDate2", "");
		baseInfoMap.put("TotalAmount2", "");
		baseInfoMap.put("DataChannel", "03");
		baseInfoMap.put("DrawingTaxCode2", "");
		baseInfoMap.put("YesNo2", "");
		baseInfoMap.put("ProjectNO", "");
		baseInfoMap.put("YesNo3", "");
		baseInfoMap.put("Amount2", "");

		d1.put("Content", "住宿");
		d1.put("Standard", "");
		d1.put("Unit", "");
		d1.put("Quantity", "4");
		d1.put("UnitAmount", "220");
		d1.put("Amount", "880");
		d1.put("TaxAmount", "");
		d1.put("Currency", "");
		d1.put("Category", "");
		d1.put("CategoryCode", "");

		details.add(d1);

		invoiceMap.put("BaseInfo", baseInfoMap);
		invoiceMap.put("Details", details);
		invoiceMap.put("ModeCode", "");

		dataMap.put("Invoice", invoiceMap);

		return dataMap;
	}
}
