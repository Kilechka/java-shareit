package booking;

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
import ru.yandex.practicum.booking.BookingController;
import ru.yandex.practicum.booking.BookingService;
import ru.yandex.practicum.booking.Status;
import ru.yandex.practicum.booking.dto.BookingDto;
import ru.yandex.practicum.booking.dto.BookingDtoIn;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.*;

@WebMvcTest(BookingController.class)
@ContextConfiguration(classes = ShareItServer.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingControllerMockMvcTest {

    private final MockMvc mockMvc;
    @MockBean
    private BookingService bookingService;
    private final ObjectMapper objectMapper;
    private BookingDto bookingDto;
    private BookingDtoIn bookingDtoIn;

    @BeforeEach
    void setUp() {
        bookingDto = BookingDto.builder().build();
        bookingDto.setId(1L);
        bookingDto.setStatus(Status.valueOf("APPROVED"));

        bookingDtoIn = BookingDtoIn.builder().build();
        bookingDtoIn.setItemId(1L);
        bookingDtoIn.setStart(LocalDateTime.parse("2024-01-01T00:00:00"));
        bookingDtoIn.setEnd(LocalDateTime.parse("2024-01-02T00:00:00"));
    }

    @Test
    void shouldCreateBooking() throws Exception {
        when(bookingService.createBooking(any(BookingDtoIn.class), anyLong())).thenReturn(bookingDto);

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDtoIn)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId().intValue())))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().name())));
    }

    @Test
    void shouldConfirmBooking() throws Exception {
        when(bookingService.confirmBooking(anyLong(), anyLong(), any(Boolean.class))).thenReturn(bookingDto);

        mockMvc.perform(patch("/bookings/{bookingId}", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId().intValue())))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().name())));
    }

    @Test
    void shouldGetBookingById() throws Exception {
        when(bookingService.getBookingById(anyLong(), anyLong())).thenReturn(bookingDto);

        mockMvc.perform(get("/bookings/{bookingId}", 1L)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId().intValue())))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().name())));
    }

    @Test
    void shouldGetBookingsOfUser() throws Exception {
        Collection<BookingDto> bookings = Arrays.asList(bookingDto);
        when(bookingService.getBookingsOfUser(anyLong(), anyString(), anyInt(), anyInt())).thenReturn(bookings);

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(bookingDto.getId().intValue())))
                .andExpect(jsonPath("$[0].status", is(bookingDto.getStatus().name())));
    }

    @Test
    void shouldGetBookingForOwner() throws Exception {
        Collection<BookingDto> bookings = Arrays.asList(bookingDto);
        when(bookingService.getBookingForOwner(anyLong(), anyString(), anyInt(), anyInt())).thenReturn(bookings);

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(bookingDto.getId().intValue())))
                .andExpect(jsonPath("$[0].status", is(bookingDto.getStatus().name())));
    }
}