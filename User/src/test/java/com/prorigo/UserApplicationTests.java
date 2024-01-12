package com.prorigo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.prorigo.bean.User;
import com.prorigo.model.response.UserResponse;
import com.prorigo.repository.UserRepository;
import com.prorigo.service.UserService;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;

@SpringBootTest
class UserApplicationTests {

	@Autowired
	private UserService userService;

	@MockBean
	private UserRepository userRepository;


	@Test
	void testGetAllBirthdays() throws Exception {

		// Arrange
		List<User> userList = Arrays.asList(new User(), new User());
		when(userRepository.findAll()).thenReturn(userList);

		// Act
		Page<UserResponse> allUsers = userService.getAllUsers(0,10,"id");

		// Assert
		assertNotNull(allUsers);
		assertEquals(userList.size(), allUsers.getTotalPages());
		verify(userRepository, times(1)).findAll();

	}


}
