#include <jni.h>
#include <string>
#include <iostream>
#include <opencv2/core/mat.hpp>
#include <opencv2/imgcodecs.hpp>
#include <opencv2/imgproc.hpp>
#include <opencv2/highgui.hpp>
#include <android/log.h>


#include <opencv2/core/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>


using namespace cv;
using namespace std;

extern "C" {
    JNIEXPORT int JNICALL
    Java_com_taek_1aaa_opencv_MainActivity_ConvertRGBtoGray(JNIEnv *env, jobject instance,
                                                        jlong matAddrInput, jlong matAddrResult, jlong path) {
        Mat binary;


        int LowH = 160;
        int HighH = 202;

        int LowS = 170;
        int HighS = 228;

        int LowV = 20;
        int HighV = 50;


        //R 값 먼저 확인하고 G 값 확인 후 B 값 확인함
        // 지금은inRange 에 matResult 를 넣어놔서 rgb로 값을 인식함
        //아래 싸이트 rgb -> hsv 변환
//http://www.rapidtables.com/convert/color/rgb-to-hsv.htm
//http://babytiger.tistory.com/entry/opencv%EC%97%90%EC%84%9C-HSV%EC%9D%98-%EA%B0%81-%EC%B1%84%EB%84%90-%EB%B2%94%EC%9C%84

        Mat &img_binary = binary;         //이렇게하면 hsv  화면
        Mat &matInput = *(Mat *)matAddrInput;
        Mat &matResult = *(Mat *)matAddrResult;
        Mat &matTmp = binary;

        //사진을 캡쳐해서 무한루프로 돌리고출력한다음에 그 사이에 색상평균값을계산해주면됨
        //인식하려면 hsv로해야 훨씬인식잘됨

        cvtColor(matInput, matTmp, CV_RGB2HSV);
        cvtColor(matTmp, matResult, CV_HSV2RGB);

        inRange(matResult, Scalar(LowH, LowS, LowV), Scalar(HighH, HighS, HighV), img_binary);
        //inRange 함수는 그 범위안에 들어가게되면 0으로 만들어주고 나머지는 1로 만들어 흑백사진을 만든다.


        //라벨링
        Mat img_labels,stats, centroids;
        int numOfLables = connectedComponentsWithStats(img_binary, img_labels,
                                                       stats, centroids, 8,CV_32S);

        //영역박스 그리기
        int max = -1, idx=0;
        for (int j = 1; j < numOfLables; j++) {
            int area = stats.at<int>(j, CC_STAT_AREA);
            if ( max < area )
            {
                max = area;
                idx = j;
            }
        }
        ///////테스트겸 지금 일단 액티비티 넘어가는 것은 꺼둠
        int left = stats.at<int>(idx, CC_STAT_LEFT);
        int top  = stats.at<int>(idx, CC_STAT_TOP);
        int width = stats.at<int>(idx, CC_STAT_WIDTH);
        int height  = stats.at<int>(idx, CC_STAT_HEIGHT);

        rectangle(matResult, Point(left, top), Point(left + width, top + height), Scalar(255, 0, 0), 3);
        //차례대로, 영상 Mat, 좌표점1, 좌표점2, 색상, 두께(-1이면 color 색상으로 채운 사각형을 그림), 타입, 시프트연산을 뜻한다.

        if(height * width < 1 || height * width > 2000000)
            return 0;
        else
            return 1;

        //   return height * width;


    }
}
