package com.onlyou.jmeter.bpc;

import com.onlyou.bpc.pub.vo.CreatePacketRequestVO;
import com.onlyou.bpc.pub.vo.CreatePacketResponseVO;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

/**
 * 创建包裹服务测试
 * <p>
 * 针对OSS上传创建包裹编号
 * </p>
 * @author HQH
 *
 */
public class CreatePacket2OssServiceTest extends BaseServiceTest {

    /**
     * 客户ID
     */
    private String customerId = "ONLYOU00000000";

    /**
     * 来源
     */
    private String source = "zyd";

    protected long getStartTime() {
        return startTime;
    }

    public SampleResult runTest(JavaSamplerContext context) {
        SampleResult sampleResult = new SampleResult();
        sampleResult.setSampleLabel("OSS上传创建包裹编号");
        sampleResult.sampleStart();
        startTime  = System.currentTimeMillis();
        //获取jmeter输入的参数值
        getParamValues(context);

        CreatePacketRequestVO requestVO = new CreatePacketRequestVO();
        requestVO.setCustomerId(customerId);
        requestVO.setSource(source);
        requestVO.setRecordFlag("0");
        
        try {
            //创建包裹
        	CreatePacketResponseVO response = super.bpcPublicService.createPacket(requestVO);
            if(response != null) {
                logger.info("包裹packetCode:" + response.getPacketCode()+", packetId："+response.getPacketId());
                sampleResult.setSuccessful(true);
                sampleResult.setResponseCodeOK();
                sampleResult.setResponseData("包裹packetCode:" + response.getPacketCode()+", packetId："+response.getPacketId()+",success:"+true, null);
                sampleResult.setDataType(SampleResult.TEXT);
            } else {
                logger.warn("create packet fail, return null.");
                sampleResult.setSuccessful(false);
                sampleResult.setResponseData("创建包裹失败" + ",success:" + false, null);
                sampleResult.setDataType(SampleResult.TEXT);
            }
        } catch (Exception e) {
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
    protected void getParamValues(JavaSamplerContext context) {
        source = context.getParameter("source", source);
        customerId = context.getParameter("customerId", customerId);
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
        return params;
    }

    public static void main(String[] args) {
        CreatePacket2OssServiceTest service = new CreatePacket2OssServiceTest();
        JavaSamplerContext context = new JavaSamplerContext(service.getDefaultParameters());
        service.setupTest(context);
        service.runTest(context);
        service.teardownTest(context);
    }

}
