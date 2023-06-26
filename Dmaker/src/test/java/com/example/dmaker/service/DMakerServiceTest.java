package com.example.dmaker.service;

import com.example.dmaker.code.StatusCode;
import com.example.dmaker.dto.CreateDeveloper;
import com.example.dmaker.dto.DeveloperDetailDto;
import com.example.dmaker.entity.Developer;
import com.example.dmaker.exception.DMakerErrorCode;
import com.example.dmaker.exception.DMakerException;
import com.example.dmaker.repository.DeveloperRepository;
import com.example.dmaker.repository.RetiredDeveloperRepository;
import com.example.dmaker.type.DeveloperLevel;
import com.example.dmaker.type.DeveloperSkillType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DMakerServiceTest {

    @Mock
    private DeveloperRepository developerRepository;

    @Mock
    private RetiredDeveloperRepository retiredDeveloperRepository;

    @InjectMocks
    private DMakerService dMakerService;

    private final Developer defaultDeveloper = Developer.builder()
            .developerLevel(DeveloperLevel.SENIOR)
            .developerSkillType(DeveloperSkillType.FRONT_END)
            .experienceYears(12)
            .statusCode(StatusCode.EMPLOYED)
            .name("name")
            .age(32)
            .build();

    private final CreateDeveloper.Request defaultRequest = CreateDeveloper.Request.builder()
            .developerLevel(DeveloperLevel.SENIOR)
            .developerSkillType(DeveloperSkillType.FRONT_END)
            .experienceYears(12)
            .memberId("memberId")
            .name("name")
            .age(32)
            .build();

    @Test
    public void testSomething() {
        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.of(defaultDeveloper));

        DeveloperDetailDto developerDetailDto = dMakerService.getDeveloperDetail("memberId");

        assertEquals(DeveloperLevel.SENIOR, developerDetailDto.getDeveloperLevel());
        assertEquals(DeveloperSkillType.FRONT_END, developerDetailDto.getDeveloperSkillType());
        assertEquals(12, developerDetailDto.getExperienceYears());
    }

    @Test
    void createDeveloperTest_success() {
        //given
        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.empty());
        ArgumentCaptor<Developer> captor = ArgumentCaptor.forClass(Developer.class);

        //when
        CreateDeveloper.Response developer = dMakerService.createDeveloper(defaultRequest);

        //then
        verify(developerRepository, times(1)).save(captor.capture());
        Developer captorValue = captor.getValue();
        assertEquals(DeveloperLevel.SENIOR, captorValue.getDeveloperLevel());
        assertEquals(DeveloperSkillType.FRONT_END, captorValue.getDeveloperSkillType());
        assertEquals(12, captorValue.getExperienceYears());
    }

    @Test
    void createDeveloperTest_failed_with_duplicate() {
        //given
        given(developerRepository.findByMemberId(anyString()))
                .willReturn(Optional.of(defaultDeveloper));
        ArgumentCaptor<Developer> captor = ArgumentCaptor.forClass(Developer.class);

        //when
        //then
        DMakerException dMakerException = assertThrows(DMakerException.class, () -> dMakerService.createDeveloper(defaultRequest));

        assertEquals(DMakerErrorCode.DUPLICATED_MEMBER_ID, dMakerException.getDMakerErrorCode());
    }
}
