package io.reactivestax.active_life_canada.controller;

import io.reactivestax.active_life_canada.dto.FamilyMemberDto;
import io.reactivestax.active_life_canada.enums.StatusLevel;
import io.reactivestax.active_life_canada.service.FamilyMemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(FamilyManagementController.class)
@AutoConfigureMockMvc
class FamilyManagementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FamilyMemberService familyMemberService;

    private FamilyMemberDto mockFamilyMember;

    @BeforeEach
    void setUp() {
        mockFamilyMember = FamilyMemberDto.builder()
                .name("suraj")
                .city("Toronto")
                .country("Canada")
                .homePhone("4373325652")
                .emailId("ssp@fullstack.com")
                .preferredContact("sms")
                .build();
    }

    @Test
    void testAddMember() throws Exception {
        when(familyMemberService.addFamilyMembers(anyLong(), any(FamilyMemberDto.class)))
                .thenReturn(mockFamilyMember);

        mockMvc.perform(post("/api/v1/family/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-family-group-id", "1")  // Required Header
                        .content("""
                                {
                                   "name": "suraj",
                                   "city": "Toronto",
                                   "country": "Canada",
                                   "homePhone": "4373325652",
                                   "emailId": "ssp@fullstack.com",
                                   "preferredContact": "sms"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("suraj"))
                .andExpect(jsonPath("$.emailId").value("ssp@fullstack.com"))
                .andExpect(jsonPath("$.homePhone").value("4373325652"));

        verify(familyMemberService, times(1)).addFamilyMembers(anyLong(), any(FamilyMemberDto.class));
    }

    @Test
    void testGetMember() throws Exception {
        when(familyMemberService.findFamilyMemberDtoById(1L))
                .thenReturn(mockFamilyMember);

        mockMvc.perform(get("/api/v1/family/members/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("suraj"))
                .andExpect(jsonPath("$.emailId").value("ssp@fullstack.com"));

        verify(familyMemberService, times(1)).findFamilyMemberDtoById(1L);
    }

    @Test
    void testUpdateMember() throws Exception {
        when(familyMemberService.updateFamilyMember(anyLong(), any(FamilyMemberDto.class)))
                .thenReturn(mockFamilyMember);

        mockMvc.perform(put("/api/v1/family/members/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                   "name": "suraj",
                                   "city": "Toronto",
                                   "country": "Canada",
                                   "homePhone": "4373325652",
                                   "emailId": "ssp@fullstack.com",
                                   "preferredContact": "sms"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("suraj"))
                .andExpect(jsonPath("$.emailId").value("ssp@fullstack.com"));

        verify(familyMemberService, times(1)).updateFamilyMember(anyLong(), any(FamilyMemberDto.class));
    }

    @Test
    void testDeleteMember() throws Exception {
        when(familyMemberService.deleteFamilyMember(1L))
                .thenReturn(StatusLevel.SUCCESS); // Assuming StatusLevel is an enum

        mockMvc.perform(delete("/api/v1/family/members/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("SUCCESS")); // Ensure response is correct

        verify(familyMemberService, times(1)).deleteFamilyMember(1L);
    }

    @Test
    void testActivateMember() throws Exception {
        when(familyMemberService.activateMemberByUuid(eq(1L), any(UUID.class)))
                .thenReturn(StatusLevel.SUCCESS);

        mockMvc.perform(post("/api/v1/family/members/1/activation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-uuid-token", UUID.randomUUID().toString())) // Random UUID
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("SUCCESS"));

        verify(familyMemberService, times(1)).activateMemberByUuid(anyLong(), any(UUID.class));
    }
}
