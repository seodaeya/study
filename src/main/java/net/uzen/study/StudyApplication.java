package net.uzen.study;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * SpringBootApplication Annotation 이 있으면, 해당 패키지와 패키지 하위 모든 파일을 컴포넌트 스캔해서 spring bean에 자동 등록한다.
 */
@SpringBootApplication
public class StudyApplication {
	public static void main(String[] args) {
		SpringApplication.run(StudyApplication.class, args);
	}
}