FROM apache/spark:3.5.1

USER 0

COPY benchmark/ /opt/spark/benchmark
COPY credit-risk-assessment.jar /tmp/credit-risk-assessment.jar
COPY spark-bench-2.3.0_0.4.0-RELEASE.jar /opt/spark