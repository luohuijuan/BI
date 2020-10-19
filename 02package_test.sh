#!/bin/sh
#功能简介：打包编译代码
#date: 20200526
#author：tanyuanji
JAVA_OPTS="-server -Xms1048m -Xmx1048m -XX:PermSize=556m -XX:MaxPermSize=556M"
rm -f base/src/main/resources/base.properties
rm -f base/src/main/resources/bankpay.properties
rm -f worker/src/main/resources/config.properties
rm -f wap/js/config.js
rm -f im/config.js
echo "测试环境 base/src/main/resources/base.properties wap/js/config.js 删除成功"

cp -f config/base_test.properties base/src/main/resources/base.properties
cp -f config/test/bankpay.properties base/src/main/resources/bankpay.properties
cp -f config/test/config_worker.properties worker/src/main/resources/config.properties
cp -f config/test/o2ologistics.properties base/src/main/resources/o2ologistics.properties
cp -f config/test/loginconnectconfig.properties base/src/main/resources/loginconnectconfig.properties
cp -f config/config_test.js wap/js/config.js
cp -f config/config_im_test.js im/config.js
echo "测试环境 base/src/main/resources/base.properties wap/js/config.js 更新成功"

cp -f config/test/config_admin.properties admin/src/main/resources/config.properties
cp -f config/test/config_api.properties api/src/main/resources/config.properties
cp -f config/test/config_apiseller.properties apiseller/src/main/resources/config.properties
cp -f config/test/config_apichain.properties apichain/src/main/resources/config.properties
cp -f config/test/config_chain.properties chain/src/main/resources/config.properties
cp -f config/test/config_seller.properties seller/src/main/resources/config.properties
#cp -f config/test/config_wap.properties wap/src/main/resources/config.properties
cp -f config/test/config_web.properties web/src/main/resources/config.properties
echo "正式环境 项目config.properties 更新成功"

#mvn clean  package -s settings.xml -Dmaven.test.skip=true
#mvn clean  package -s settings.xml   -U -B  -T 1C -Dmaven.test.skip=true -Dmaven.compile.fork=true

#echo "清理META-INF for worker"
#if [ -f "./worker/target/b2b2c.jar" ]; then
#    zip -d "./worker/target/b2b2c.jar" 'META-INF/.SF' 'META-INF/.RSA' 'META-INF/*SF'
#fi