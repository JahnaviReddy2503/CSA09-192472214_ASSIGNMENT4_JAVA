#!/usr/bin/env bash
set -u
set -o pipefail
mkdir -p Task01/out Task02/out Task03/out Task04/out Task05/out
javac -d Task01/out Task01/src/*.java && java -cp Task01/out FeeServiceTest > Task01/output/output.txt
javac -d Task02/out Task02/src/*.java && java -cp Task02/out AppointmentDesk --test > Task02/output/output.txt
javac -d Task03/out Task03/src/*.java && java -cp Task03/out LibrarySearch --test > Task03/output/output.txt
javac -d Task04/out Task04/src/*.java && java -cp Task04/out DrawPadTest > Task04/output/before_after_minimise.txt
javac -d Task05/out Task05/src/*.java && java -cp Task05/out ConverterTest > Task05/output/valid_conversion.txt
echo "Task 6: NOT EXECUTED – MySQL Connector/J and server must be configured." > Task06/output/test_cases.txt
