package com.vanguard.trade.reporting.engine.controller;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.vanguard.trade.reporting.engine.bean.EventResponse;
import com.vanguard.trade.reporting.engine.exception.ApiRequestException;
import com.vanguard.trade.reporting.engine.exception.ResourceNotFoundException;
import com.vanguard.trade.reporting.engine.model.FilterEvent;
import com.vanguard.trade.reporting.engine.service.EventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/events")
@Slf4j
public class EventController {

    @Autowired
    private EventService eventService;

//    @GetMapping
//    public ArrayNode getAllEvents() {
//        return eventService.getEventsBasedOnCriteria();
//    }

    @GetMapping
    public ArrayNode getEvent(@RequestParam("fileName") String fileName) throws ResourceNotFoundException {
        ArrayNode arrayNode = eventService.getEventBasedOnCriteria(fileName);

        if (arrayNode.isEmpty()) {
            throw new ResourceNotFoundException("File/Event with name '" + fileName + "' does not match with any criteria");
        }

        return arrayNode;
    }

	@PostMapping
	public List<FilterEvent> handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
	    String fileName = file.getOriginalFilename();
	    if (eventService.fileAlreadyParsed(fileName)) {
	        log.error("File '" + fileName + "' is already parsed.");
	        throw new ApiRequestException("File '" + fileName + "' is already parsed.");
        }

        try {
            return eventService.processFile(fileName, file.getInputStream());
        } catch (IOException e) {
            throw new ApiRequestException("File '" + fileName + "' is not parsed." + e.getMessage());
        }

	}
}
