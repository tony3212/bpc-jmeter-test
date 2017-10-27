package com.onlyou.jmeter.bpc;

import com.onlyou.bpc.bpo.vo.CreatePacketRequestVO;
import com.onlyou.bpc.bpo.vo.CreatePacketResponseVO;
import com.onlyou.bpc.external.enumerator.YesNoEnum;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

/**
 * @Author : yao
 * @Date : 15:10 2017/10/15
 * @Description : 创建包裹服务测试
 * <p>
 * 针对FTP上传创建包裹编号
 * </p>
 */
public class CreatePacket2FtpServiceTest extends BaseServiceTest {

    /**
     * 客户ID
     */
    private String customerId = "ONLYOU00000000";

    /**
     * 来源
     */
    private String source = "zyd";

    /**
     * 票据数据
     */
    private int billCount = 10;

    private String partnerId;

    private String recordFlag;

    protected long getStartTime() {
        return startTime;
    }

    public SampleResult runTest(JavaSamplerContext context) {
        SampleResult sampleResult = new SampleResult();
        sampleResult.setSampleLabel("FTP上传创建包裹编号");
        sampleResult.sampleStart();
        startTime  = System.currentTimeMillis();
        //获取jmeter输入的参数值
        getParamValues(context);

        CreatePacketRequestVO createPacketRequestVO = new CreatePacketRequestVO();
        createPacketRequestVO.setBranchNum(branchNum);
        createPacketRequestVO.setSource(getSource(source));
        createPacketRequestVO.setBillCount(billCount);
        createPacketRequestVO.setCustomerId(customerId);
        createPacketRequestVO.setPartnerId(partnerId);
        createPacketRequestVO.setRecordFlag(YesNoEnum.getByCode(recordFlag));
        try {
            //创建包裹
            CreatePacketResponseVO response = super.bpoBillService.createPacket(createPacketRequestVO);
            if(response != null) {
                logger.info("包裹号:" + response.getPacketCode());
                sampleResult.setSuccessful(true);
                sampleResult.setResponseCodeOK();
                sampleResult.setResponseData("包裹号:" + response.getPacketCode()+",success:"+true, null);
                sampleResult.setDataType(SampleResult.TEXT);
            } else {
                logger.warn("create packet fail, return null.");
                sampleResult.setSuccessful(false);
                sampleResult.setResponseData("创建包裹失败"+",success:"+false, null);
                sampleResult.setDataType(SampleResult.TEXT);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            sampleResult.setSuccessful(false);
            sampleResult.setResponseData(e.getMessage()+",success:"+false, null);
            sampleResult.setDataType(SampleResult.TEXT);
        } finally {
            sampleResult.sampleEnd(); // 结束统计响应时间标记
        }
        return sampleResult;
    }

    /**
     * 获取jmeter输入的参数值
     */
    protected void getParamValues(JavaSamplerContext context) {
        source = context.getParameter("source", source);
        customerId = context.getParameter("customerId", customerId);
        billCount  = Integer.parseInt(context.getParameter("billCount", billCount + ""));
        partnerId = context.getParameter("partnerId", "");
        recordFlag = context.getParameter("recordFlag", YesNoEnum.YES.getCode());
    }

    /**
     * 设置可用参数及默认值
     *
     * @return
     */
    @Override
    public Arguments getDefaultParameters() {
        Arguments params = new Arguments();
        params.addArgument("source", source);
        params.addArgument("customerId", customerId);
        params.addArgument("billCount", billCount + "");
        params.addArgument("partnerId", "2c94818b4e1f244e014e249177be05d1");
        params.addArgument("recordFlag", YesNoEnum.YES.getCode());
        return params;
    }

    public static void main(String[] args) {
        CreatePacket2FtpServiceTest service = new CreatePacket2FtpServiceTest();
        JavaSamplerContext context = new JavaSamplerContext(service.getDefaultParameters());
        service.setupTest(context);
        service.runTest(context);
        service.teardownTest(context);
    }

}
