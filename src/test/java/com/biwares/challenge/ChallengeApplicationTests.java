package com.biwares.challenge;

import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ChallengeApplicationTests {

	@Test
	void contextLoads() {
	}

	static int[][] matriz1 = {{10, 100, 10}, {1, 10, 1}, {1, 10, 1}};
	static int[][] matriz2 = {{6, 25, 4, 10}, {12, 25, 1, 15}, {7, 15, 15, 5}};

	@Test
	void processMatrix3x3AndReturnInt() {
		assert Solucion.solucion(matriz1, 3, 3) == 5;
	}

	@Test
	void processMatrix3x4AndReturnInt() {
		assert Solucion.solucion(matriz2, 3, 4) == 4;
	}

}
