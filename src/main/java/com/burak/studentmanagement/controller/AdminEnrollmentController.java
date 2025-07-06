package com.burak.studentmanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.burak.studentmanagement.entity.EnrollmentRequest;
import com.burak.studentmanagement.entity.EnrollmentRequest.EnrollmentStatus;
import com.burak.studentmanagement.entity.Teacher;
import com.burak.studentmanagement.service.EnrollmentRequestService;
import com.burak.studentmanagement.service.TeacherService;

@Controller
@RequestMapping("/admin/enrollment")
public class AdminEnrollmentController {

    @Autowired
    private EnrollmentRequestService enrollmentRequestService;

    @Autowired
    private TeacherService teacherService;

    @GetMapping("/requests")
    public String showAllEnrollmentRequests(Model theModel) {
        List<EnrollmentRequest> allRequests = enrollmentRequestService.findAll();
        List<EnrollmentRequest> pendingRequests = enrollmentRequestService.findByStatus(EnrollmentStatus.PENDING);
        List<EnrollmentRequest> approvedRequests = enrollmentRequestService.findByStatus(EnrollmentStatus.APPROVED);
        List<EnrollmentRequest> rejectedRequests = enrollmentRequestService.findByStatus(EnrollmentStatus.REJECTED);

        theModel.addAttribute("allRequests", allRequests);
        theModel.addAttribute("pendingRequests", pendingRequests);
        theModel.addAttribute("approvedRequests", approvedRequests);
        theModel.addAttribute("rejectedRequests", rejectedRequests);

        return "admin/enrollment-requests";
    }

    @GetMapping("/requests/pending")
    public String showPendingRequests(Model theModel) {
        List<EnrollmentRequest> pendingRequests = enrollmentRequestService.findByStatus(EnrollmentStatus.PENDING);
        theModel.addAttribute("pendingRequests", pendingRequests);
        theModel.addAttribute("title", "Pending Enrollment Requests");
        return "admin/pending-enrollment-requests";
    }

    @GetMapping("/requests/approved")
    public String showApprovedRequests(Model theModel) {
        List<EnrollmentRequest> approvedRequests = enrollmentRequestService.findByStatus(EnrollmentStatus.APPROVED);
        theModel.addAttribute("approvedRequests", approvedRequests);
        theModel.addAttribute("title", "Approved Enrollment Requests");
        return "admin/processed-enrollment-requests";
    }

    @GetMapping("/requests/rejected")
    public String showRejectedRequests(Model theModel) {
        List<EnrollmentRequest> rejectedRequests = enrollmentRequestService.findByStatus(EnrollmentStatus.REJECTED);
        theModel.addAttribute("rejectedRequests", rejectedRequests);
        theModel.addAttribute("title", "Rejected Enrollment Requests");
        return "admin/processed-enrollment-requests";
    }

    @GetMapping("/request/{requestId}")
    public String showEnrollmentRequestDetails(@PathVariable("requestId") int requestId, Model theModel) {
        EnrollmentRequest request = enrollmentRequestService.findById(requestId);

        if (request == null) {
            theModel.addAttribute("error", "Enrollment request not found.");
            return "admin/enrollment-error";
        }

        theModel.addAttribute("request", request);
        return "admin/enrollment-request-details";
    }

    @PostMapping("/request/{requestId}/approve")
    public String approveEnrollmentRequest(@PathVariable("requestId") int requestId,
            @RequestParam("teacherId") int teacherId,
            @RequestParam(value = "notes", required = false) String notes,
            Model theModel) {

        try {
            enrollmentRequestService.approveRequest(requestId, teacherId, notes);
            theModel.addAttribute("success", "Enrollment request approved successfully.");
        } catch (Exception e) {
            theModel.addAttribute("error", "Failed to approve enrollment request: " + e.getMessage());
        }

        return "redirect:/admin/enrollment/requests/pending";
    }

    @PostMapping("/request/{requestId}/reject")
    public String rejectEnrollmentRequest(@PathVariable("requestId") int requestId,
            @RequestParam("teacherId") int teacherId,
            @RequestParam(value = "notes", required = false) String notes,
            Model theModel) {

        try {
            enrollmentRequestService.rejectRequest(requestId, teacherId, notes);
            theModel.addAttribute("success", "Enrollment request rejected successfully.");
        } catch (Exception e) {
            theModel.addAttribute("error", "Failed to reject enrollment request: " + e.getMessage());
        }

        return "redirect:/admin/enrollment/requests/pending";
    }

    @PostMapping("/request/{requestId}/delete")
    public String deleteEnrollmentRequest(@PathVariable("requestId") int requestId, Model theModel) {
        try {
            enrollmentRequestService.deleteById(requestId);
            theModel.addAttribute("success", "Enrollment request deleted successfully.");
        } catch (Exception e) {
            theModel.addAttribute("error", "Failed to delete enrollment request: " + e.getMessage());
        }

        return "redirect:/admin/enrollment/requests";
    }
}