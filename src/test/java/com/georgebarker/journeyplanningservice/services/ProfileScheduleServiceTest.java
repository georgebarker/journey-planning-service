package com.georgebarker.journeyplanningservice.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.Optional;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.georgebarker.journeyplanningservice.dao.ProfileScheduleDao;
import com.georgebarker.journeyplanningservice.model.ProfileSchedule;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ProfileScheduleServiceTest {

    @MockBean
    ProfileScheduleDao profileScheduleDao;

    @Autowired
    ProfileScheduleService profileScheduleService;

    private Long dayType = 1L;
    private DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
    private DateTime mockDateTime = formatter.parseDateTime("2019-07-02");
    private Date mockDate = mockDateTime.toDate();
    private ProfileSchedule profileSchedule = setupMockProfileSchedule();
    private Optional<ProfileSchedule> optionalProfileSchedule = Optional.of(profileSchedule);

    @Test
    public void testGetDayTypeIdForDateTime() {
        when(profileScheduleDao.findById(mockDate)).thenReturn(optionalProfileSchedule);
        Long returnedDayType = profileScheduleService.getDayTypeIdForDateTime(mockDateTime);
        assertEquals(dayType, returnedDayType);
    }
    
    private ProfileSchedule setupMockProfileSchedule() {
        ProfileSchedule profileSchedule = new ProfileSchedule();
        profileSchedule.setDayTypeId(dayType);
        profileSchedule.setProfileDate(mockDate);
        return profileSchedule;
    }
}
