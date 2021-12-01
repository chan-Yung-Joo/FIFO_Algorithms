package Project.Computer_Arch.FIFO;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class FIFO_Algo {
    public final static int NON_VALUE = 99999; // 배열이 비어있음을 나타내기 위해 임의의 의미없는 수로 셋팅하기 위한 상수를 선언

    // cache block이 꽉차있는지 여부에 관한 함수
    public static boolean isFull(int [] arr){
        for(int n: arr){ // 배열에 있는 모든 데이터 중에서..
            if(n == NON_VALUE){ // 특정 데이터가 NON_VALUE(비어 있음을 나타내는 값)이면
                // 아직 공간이 남아있으므로 false를 반환
                return false;
            }
        }
        // 위의 과정을 다 마치면(즉, 배열이 꽉차 있으면) true를 반환함
        return true;
    }

    // 새로 들어온 input 데이터가 이미 cache block에 있는지 확인하는 함수
    // 즉, Hit 여부를 확인 하는 함수임
    public static boolean isHit(int [] array, int num){

        for(int n: array){ // 배열에 있는 모든 데이터 중에서..
            if(n == num){ // 특정 데이터의 값이 새로운 데이터의 값과 동일하면
                // 이 경우는 Hit 이므로, true를 반환하도록 함
                return true;
            }
        }
        // 위의 과정을 다 마치면(즉, 특정 데이터의 값이 새로운 데이터의 값과 동일한 것이 존재하지 않는다면)
        // false를 반환
        return false;
    }

    public static void main(String[] args) throws FileNotFoundException {

        List<Integer> input = new ArrayList<Integer>(); // 파일에서 읽어온 데이터를 저장하기 위한 List 선언

        int [] cache_block = { NON_VALUE, NON_VALUE, NON_VALUE, NON_VALUE }; // 캐시 블록을 의미하는 배열 선언(캐시 블록 수가 4개 이므로, 사이즈가 4인 배열)

        int index = 0; // 교체가 이루어질 위치를 지정하기 위한 인덱스 변수
        int pointer = 0; // 캐시가 꽉 차기 전, 넣을 위치를 지정하기 위한 인덱스 변수
        int hitCount = 0; // 적중한 개수
        double ratio = 0.0; // 적중률을 위한 변수

        // 파일 I/O
        Scanner scanner = new Scanner(new File("/Users/joochanyung/IdeaProjects/easyjava/src/Project/Computer_Arch/FIFO/input(샘플).txt"));
        while(scanner.hasNext()){
            String str = scanner.next(); // 데이터를 문자열 단위로 읽어 온 다음...
            input.add(Integer.parseInt(str)); // 그 데이터를 정수형으로 변환하여 input 리스트에 추가
        }

        System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");

        for(int i=0; i<input.size(); i++){

            // 만약, input 데이터가 99이면...
            if (input.get(i) == 99) {
                break; // 종료하도록 한다.
            }

            // 만약, input 데이터가 99가 아니라면
            // 작업을 수행하면 됨
            else {

                // 먼저, 확인해야 할 것은 캐시 블록이 꽉 찼는지 여부를 확인해야 함
                // 만약, 캐시 블록이 꽉 차지 않은 경우!
                if (!isFull(cache_block)) {

                    // 만약, 새로 들어오는 input 데이터가 이미 cache block에 있는 경우
                    // 즉, Hit 인 경우!
                    if(isHit(cache_block, input.get(i))){
                        hitCount += 1;
                        System.out.println("- - - - - - - - - - - - - - - - - - || Hit 발생 || - - - - - - - - - - - - - - - - - - -");
                    }

                    // 만약, 새로 들어오는 input 데이터가 이미 cache block에 없는 경우
                    // 즉, Hit가 아닌 경우!
                    else{
                        cache_block[pointer] = input.get(i); // 들어갈 위치를 가지고 있는 pointer의 값을 이용하여 그 위치에 데이터 삽입
                        pointer++; // 그런 뒤, 들어갈 위치를 1 증가
                    }
                }


                // 만약, cache block이 꽉 찬 경우!
                // 교체 작업을 수행하면 된다.
                else {
                    // 먼저, 새로 들어오는 input 데이터가 이미 cache block에 있는 경우
                    // 즉, Hit 인 경우!
                    if (isHit(cache_block, input.get(i))) {
                        hitCount += 1; // hit한 개수를 1 증가 시킴
                        System.out.println("- - - - - - - - - - - - - - - - - - || Hit 발생 || - - - - - - - - - - - - - - - - - - -");
                    }

                    // 만약, 새로 들어오는 input 데이터가 cache block에 없는 경우
                    // 즉, hit가 아닌 경우!
                    else {
                        // input 데이터를 cache block에 대입을 한다.
                        // 이때, index는 먼저 들어온 데이터가 위치해 있는 인덱스를 담고 있다.
                        // index 변수의 범위는 0~3까지 이므로,
                        // 3보다 큰 경우에는 index 값을 다시 0으로 돌아가도록 해줌 -> 즉, 0~3까지 순환하도록 만들어줌
                        cache_block[index] = input.get(i);

                        index++; // input 데이터를 cache block에 대입후, 먼저 들어온 데이터의 위치를 가르키는 인덱스를 증가 시킴
                        if(index > 3 ) { index = 0; }
                    }

                }

                ratio = hitCount / ((double)(input.size()-1));

                // 이 부분은 출력하는 부분임

                for (int data : cache_block) {
                    System.out.printf("%8d  | ", data);
                }
                System.out.printf("Count of Hit : %d  | Hit Rate : %.2f%%  |" , hitCount, ratio*100);
                System.out.println();
            }
        }

        System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");

        System.out.printf("* 최종 Hit 개수  : %d개 \n* 최종 Hit Rate : %.2f%%\n", hitCount, ratio*100);

        System.out.println("- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -");
    }
}


