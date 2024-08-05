package request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.ShareItServer;
import ru.yandex.practicum.request.RequestController;
import ru.yandex.practicum.request.RequestService;
import ru.yandex.practicum.request.dto.RequestDto;
import ru.yandex.practicum.request.dto.RequestDtoOut;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RequestController.class)
@ContextConfiguration(classes = ShareItServer.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RequestControllerMockMvcTest {

    private final MockMvc mockMvc;
    @MockBean
    private RequestService requestService;

    private final ObjectMapper objectMapper;
    private RequestDto requestDto;
    private RequestDtoOut requestDtoOut;

    @BeforeEach
    void setUp() {
        requestDto = new RequestDto();
        requestDto.setId(1L);
        requestDto.setDescription("Test request");

        requestDtoOut = RequestDtoOut.builder()
                        .id(1L)
                        .description("Test request")
                        .build();
    }

    @Test
    void shouldCreateRequest() throws Exception {
        when(requestService.createRequest(anyLong(), any(RequestDto.class))).thenReturn(requestDto);

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requestDto.getId().intValue())))
                .andExpect(jsonPath("$.description", is(requestDto.getDescription())));
    }

    @Test
    void shouldGetUserRequests() throws Exception {
        Collection<RequestDtoOut> requests = Arrays.asList(requestDtoOut);
        when(requestService.getUserRequests(anyLong())).thenReturn(requests);

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(requestDtoOut.getId().intValue())))
                .andExpect(jsonPath("$[0].description", is(requestDtoOut.getDescription())));
    }

    @Test
    void shouldGetRequests() throws Exception {
        Collection<RequestDto> requests = Arrays.asList(requestDto);
        when(requestService.getRequests(anyLong(), anyInt(), anyInt())).thenReturn(requests);

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1L)
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(requestDto.getId().intValue())))
                .andExpect(jsonPath("$[0].description", is(requestDto.getDescription())));
    }

    @Test
    void shouldGetRequestById() throws Exception {
        when(requestService.getRequest(anyLong(), anyLong())).thenReturn(requestDtoOut);

        mockMvc.perform(get("/requests/{requestId}", 1L)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requestDtoOut.getId().intValue())))
                .andExpect(jsonPath("$.description", is(requestDtoOut.getDescription())));
    }
}