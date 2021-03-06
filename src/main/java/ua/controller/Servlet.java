package ua.controller;

import ua.controller.command.*;
import ua.controller.command.Exception;
import ua.controller.command.admin.AdminAccount;
import ua.controller.command.admin.BookingRequests;
import ua.controller.command.admin.ChooseApartment;
import ua.controller.command.admin.ReservationRequest;
import ua.controller.command.user.*;
import ua.model.service.Impl.BookingServiceImpl;
import ua.model.service.Impl.RequestServiceImpl;
import ua.model.service.Impl.RoomServiceImpl;
import ua.model.service.Impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Servlet extends HttpServlet {
    private Map<String, Command> commands = new HashMap<>();

    public void init() {
        commands.put("logout", new LogOut());
        commands.put("login", new Login(new UserServiceImpl()));
        commands.put("registration", new Registration());
        commands.put("exception", new Exception());
        commands.put("user/user_account", new UserAccount());
        commands.put("admin/admin_account", new AdminAccount());
        commands.put("register", new RegisterUser(new UserServiceImpl()));
        commands.put("user/booking_request", new BookingRequestForm());
        commands.put("user/register_request", new RegisterRequest(new RequestServiceImpl()));
        commands.put("user/rooms", new Rooms(new RoomServiceImpl()));
        commands.put("admin/booking_request", new BookingRequests(new RequestServiceImpl()));
        commands.put("admin/choose_apartments",new ChooseApartment(new RoomServiceImpl(),new RequestServiceImpl()));
        commands.put("admin/reservation_request",new ReservationRequest(new BookingServiceImpl()));
        commands.put("user/pay_booking",new PayBooking());
    }

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws IOException, ServletException {
        processRequest(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        path = path.replaceAll(".*/app/", "");
        Command command = commands.getOrDefault(path,
                (r) -> "/view/unsupportedCommand)");
        String page = command.execute(request);
        if (page.startsWith("redirect:")) {
            response.sendRedirect(page.replace("redirect:", ""));
        } else {
            request.getRequestDispatcher(page).forward(request, response);
        }
    }
}