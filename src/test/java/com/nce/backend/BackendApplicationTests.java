package com.nce.backend;

import com.tngtech.archunit.core.domain.JavaClass;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.modulith.core.ApplicationModules;

@SpringBootTest
class BackendApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void verifyModularity() {
		ApplicationModules modules = ApplicationModules.of(BackendApplication.class, JavaClass.Predicates.resideInAnyPackage("com.nce.backend.common.."));
		modules.verify();
	}

}
