/*
 *   Copyright 2018 . AppDynamics LLC and its affiliates.
 *   All Rights Reserved.
 *   This is unpublished proprietary source code of AppDynamics LLC and its affiliates.
 *   The copyright notice above does not evidence any actual or intended publication of such source code.
 *
 */

package com.appdynamics.extensions.aws.elb;

import com.appdynamics.extensions.conf.ControllerInfo;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import com.appdynamics.extensions.dashboard.CustomDashboardJsonUploader;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static com.appdynamics.extensions.aws.elb.Constants.NORMAL_DASHBOARD;
import static com.appdynamics.extensions.aws.elb.Constants.SIM_DASHBOARD;

/**
 * Created by bhuvnesh.kumar on 7/17/18.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Dashboard.class)
public class DashboardTest {

    private static final Logger logger = Logger.getLogger(DashboardTest.class);

    private String dashboardJson ;
    private Map dashboardMap = new HashMap();

    @Mock
    private ControllerInfo controllerInfo;

    private void creatDashboardMap(){
        try {
            dashboardMap.put(NORMAL_DASHBOARD, FileUtils.readFileToString(new File("/Users/bhuvnesh.kumar/repos/appdynamics/extensions/aws-elb-monitoring-extension/src/test/resources/conf/normalDashboard.json")));
            dashboardMap.put(SIM_DASHBOARD, FileUtils.readFileToString(new File("/Users/bhuvnesh.kumar/repos/appdynamics/extensions/aws-elb-monitoring-extension/src/test/resources/conf/simDashboard.json")));
        } catch (Exception e){
            Assert.fail();
        }
    }

    private Map valueMap(Map config){
        config.put("enabled","true");
        config.put("namePrefix","Prefix");
        config.put("uploadDashboard","true");
        config.put("executionFrequencyMinutes","5");
        config.put("host","localhost");
        config.put("port","8090");
        config.put("account","customer");
        config.put("password","root");
        config.put("username","user");
        config.put("accountAccessKey","pass");
        config.put("applicationName","app");
        config.put("tierName","tier");
        config.put("nodeName","node");
        return config;
    }

    private void getJsonAsString()  {
        try {
            dashboardJson = FileUtils.readFileToString(new File("src/test/resources/conf/dashboard.json"));

        } catch (Exception e){
            logger.error("Error in file reading");
        }
    }

    private ControllerInfo setUpControllerInfoWithoutSim(ControllerInfo controllerInfo){
        controllerInfo.setUsername("admin");
        controllerInfo.setPassword("root");
        controllerInfo.setAccountAccessKey("keyisthisthing");
        controllerInfo.setAccount("customer1");
        controllerInfo.setControllerHost("Host");
        controllerInfo.setControllerPort(8090);
        controllerInfo.setControllerSslEnabled(false);
        controllerInfo.setMachinePath("MachinePath|Something");
        controllerInfo.setSimEnabled(false);
        controllerInfo.setApplicationName("App");
        controllerInfo.setTierName("Tier");
        controllerInfo.setNodeName("Node");

        return controllerInfo;
    }

    private ControllerInfo setUpControllerInfoWithSim(ControllerInfo controllerInfo){
        controllerInfo.setUsername("admin");
        controllerInfo.setPassword("root");
        controllerInfo.setAccountAccessKey("keyisthisthing");
        controllerInfo.setAccount("customer1");
        controllerInfo.setControllerHost("Host");
        controllerInfo.setControllerPort(8090);
        controllerInfo.setControllerSslEnabled(false);
        controllerInfo.setMachinePath("MachinePath|Something");
        controllerInfo.setSimEnabled(true);
        return controllerInfo;
    }


    @Test
    public void testDashboard(){

        ControllerInfo controllerInformation = new ControllerInfo();
        controllerInformation = setUpControllerInfoWithoutSim(controllerInformation);

        Map config = new HashMap();
        config = valueMap(config);
        getJsonAsString();
        creatDashboardMap();

        CustomDashboardJsonUploader customDashboardJsonUploader = Mockito.mock(CustomDashboardJsonUploader.class);
        Mockito.doNothing().when(customDashboardJsonUploader).uploadDashboard(config.get("namePrefix").toString(), dashboardJson, config, false);
        Mockito.when(controllerInfo.getControllerInfo()).thenReturn(controllerInformation);
        try{
            Dashboard dashboard = new Dashboard(config, dashboardMap, customDashboardJsonUploader, controllerInfo);
            dashboard.sendDashboard();
        }
        catch (Exception e){
            Assert.fail();
        }
    }

    @Test
    public void testSimAndNotSim(){

    }

}
