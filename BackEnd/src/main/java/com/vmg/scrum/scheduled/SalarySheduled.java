package com.vmg.scrum.scheduled;

import com.vmg.scrum.model.*;
import com.vmg.scrum.model.excel.LogCheckInOut;
import com.vmg.scrum.model.excel.LogDetail;
import com.vmg.scrum.model.option.NoteLog;
import com.vmg.scrum.model.request.Request;
import com.vmg.scrum.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;

@Service
public class SalarySheduled {
    @Autowired
    SignRepository signRepository;
    @Autowired
    LogDetailRepository logDetailRepository;
    @Autowired
    HolidayRepository holidayRepository;
    @Autowired
    RequestRepository requestRepository;
    @Autowired
    ApproveSttRepository approveSttRepository;
    @Autowired
    NoteCatergoryRepository noteCatergoryRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    LogCheckInOutRepository logCheckInOutRepository;

    @Scheduled(cron = "0 03 9 * * *")
    public void readLogYesterday(){
        LocalDate currentDate = LocalDate.now();
        LocalDate yesterday = currentDate.minusDays(0);
        LocalDate firstDayOfMonth = LocalDate.of(currentDate.getYear(), currentDate.getMonth(), 1);



        // Holiday CASE
        Holiday holidayYesterday = holidayRepository.findCurrentDate(yesterday.toString());
        if (holidayYesterday != null) {
            List<User> users = userRepository.findAll();
            for (User user : users) {
                System.out.println(user.getCode() + " Add Log HOLIDAY");
                // create log
                LogDetail logDetail = new LogDetail();
                Set<NoteLog> noteCatergorySet = logDetail.getNoteLogSet();
                if (noteCatergorySet == null)
                    noteCatergorySet = new HashSet<>();
                NoteLog noteLog = new NoteLog();
                noteLog.setLogDetail(logDetail);
                noteLog.setNoteCatergory(noteCatergoryRepository.findByName(ENoteCatergory.E_HOLIDAY));
                noteLog.setContent(holidayYesterday.getHolidayName());
                noteLog.setCreateDate(LocalDateTime.now());
                noteLog.setSignChange(signRepository.findByName(ESign.L));
                noteCatergorySet.add(noteLog);

                logDetail.setNoteLogSet(noteCatergorySet);
                logDetail.setSigns(signRepository.findByName(ESign.L));
                logDetail.setDateLog(yesterday);
                logDetail.setUser(user);
//                logDetail.setRequestActive(true);
                logDetailRepository.save(logDetail);
            }
            return;
        }

        // NT CASE
        if (yesterday.getDayOfWeek().toString() == "SATURDAY" ||
                yesterday.getDayOfWeek().toString() == "SUNDAY") {
            List<User> users = userRepository.findAll();
            for (User user : users) {
                System.out.println(user.getCode() + " Add Log SS");
                // create log
                LogDetail logDetail = new LogDetail();
                Set<NoteLog> noteCatergorySet = logDetail.getNoteLogSet();
                if (noteCatergorySet == null)
                    noteCatergorySet = new HashSet<>();
                NoteLog noteLog = new NoteLog();
                noteLog.setLogDetail(logDetail);
                noteLog.setNoteCatergory(noteCatergoryRepository.findByName(ENoteCatergory.E_WEEKEND));
//                noteLog.setContent(holidayYesterday.getHolidayName());
                noteLog.setCreateDate(LocalDateTime.now());
                noteLog.setSignChange(signRepository.findByName(ESign.NT));
                noteCatergorySet.add(noteLog);
                logDetail.setNoteLogSet(noteCatergorySet);
                logDetail.setSigns(signRepository.findByName(ESign.NT));
                logDetail.setDateLog(yesterday);
                logDetail.setUser(user);
//                logDetail.setRequestActive(true);
                logDetailRepository.save(logDetail);
            }
            return;
        }


        // check check in/ check out từ LogCheckInOut
        List<LogCheckInOut> checkInOuts = logCheckInOutRepository.findByDate(yesterday);

        for (LogCheckInOut log: checkInOuts) {

            if(log.getUser()!=null){
                LogDetail logDetail = new LogDetail();
                LogDetail logDetailExist = logDetailRepository.findByUserCodeAndDate(log.getUser().getCode(), log.getDateCheck());

                //check log Detail đã tồn tại
                if(logDetailExist!=null){
                    // time out == null  => setTimeOut
                    if(logDetailExist.getTimeOut()==null){
                        logDetailExist.setTimeOut(log.getTimeCheck());
                        Duration duration = Duration.between(logDetailExist.getTimeIn(), logDetailExist.getTimeOut());

                        LocalTime total = LocalTime.of((int)duration.toHours(),
                                (int)(duration.toMinutes() % 60),
                                (int)(duration.toSeconds() % 60));
                        logDetailExist.setTotalWork(total.minusHours(1));
                    }
                    // nếu time check sau TimeOut của log thì set lại
                    else {
                        if(logDetailExist.getTimeIn()==null ){
                            logDetailExist.setTimeOut(log.getTimeCheck());
                        }
                        else if(logDetailExist.getTimeOut().isBefore(log.getTimeCheck())){
                            logDetailExist.setTimeOut(log.getTimeCheck());
                        Duration duration = Duration.between(logDetailExist.getTimeIn(), logDetailExist.getTimeOut());
                        LocalTime total = LocalTime.of((int)duration.toHours(),
                                (int)(duration.toMinutes() % 60),
                                (int)(duration.toSeconds() % 60));
                            logDetailExist.setTotalWork(total.minusHours(1));
                        }
                    }
                    logDetailRepository.save(logDetailExist);

                }
                else {
                    logDetail = new LogDetail();
                    logDetail.setDateLog(yesterday);
                    if(log.getUser()!=null)  logDetail.setUser(log.getUser());

                    if(log.getTimeCheck().isAfter(LocalTime.of(12,0,0))){
                        logDetail.setTimeOut(log.getTimeCheck());
                    }else{
                        if(logDetail.getTimeIn()==null){
                            logDetail.setTimeIn(log.getTimeCheck());
                        }
                    }
                    logDetailRepository.save(logDetail);
                }
            }
        }
        for (User user: userRepository.findAll()) {
            LogDetail logDetailExist = logDetailRepository.findByUserCodeAndDate(user.getCode(), yesterday);
            if( logDetailExist ==null){
                LogDetail logDetail = new LogDetail();
                logDetail.setDateLog(yesterday);
                logDetail.setSigns(signRepository.findByName(ESign.KL));
                logDetail.setUser(user);
//                logDetail.setRequestActive(true);
                logDetailRepository.save(logDetail);
            }

        }


        List<Request> requests = requestRepository.findByStatusAndDateList(2,firstDayOfMonth, yesterday);
        List<LogDetail> logDetails = logDetailRepository.findByCurrentDay(yesterday);

        for (LogDetail logDetail : logDetails) {
            Sign currentSign = logDetail.getSigns();
            if (holidayYesterday != null) {
                logDetail.setSigns(signRepository.findByName(ESign.L));
                Set<NoteLog> noteCatergorySet = logDetail.getNoteLogSet();
                if (noteCatergorySet == null)
                    noteCatergorySet = new HashSet<>();
                NoteLog noteLog = new NoteLog();
                noteLog.setLogDetail(logDetail);
                noteLog.setNoteCatergory(noteCatergoryRepository.findByName(ENoteCatergory.E_HOLIDAY));
                noteLog.setContent(holidayYesterday.getHolidayName());
                noteLog.setLastSign(logDetail.getSigns());
                noteLog.setCreateDate(LocalDateTime.now());
                noteLog.setSignChange(signRepository.findByName(ESign.L));
                noteCatergorySet.add(noteLog);
                logDetail.setNoteLogSet(noteCatergorySet);
                logDetail.setSigns(signRepository.findByName(ESign.L));
                logDetailRepository.save(logDetail);
                continue;
            }

            DayOfWeek dayOfWeek = logDetail.getDateLog().getDayOfWeek();
            Integer hourIn = null;
            Integer hourOut = null;
            Integer minuteIn = null;
            Integer minuteOut = null;
            Integer secondIn = null;
            Integer secondOut = null;
            if (logDetail.getTimeIn() != null) {
                hourIn = logDetail.getTimeIn().getHour();
                minuteIn = logDetail.getTimeIn().getMinute();
                secondIn = logDetail.getTimeIn().getSecond();
                System.out.println("Hour in : " + hourIn);
                System.out.println("Minute in : " + minuteIn);
                System.out.println("Second in : " + secondIn);

            }
            if (logDetail.getTimeOut() != null) {
                hourOut = logDetail.getTimeOut().getHour();
                minuteOut = logDetail.getTimeOut().getMinute();
                secondOut = logDetail.getTimeOut().getSecond();

                System.out.println("Hour out : " + hourOut);
                System.out.println("Minute out : " + minuteOut);
                System.out.println("Second out : " + secondOut);

            }
            System.out.println(logDetail.getDateLog() + "-" + dayOfWeek);

            if (hourOut == null && hourIn == null) {
                if (!dayOfWeek.toString().equals("SUNDAY") && !dayOfWeek.toString().equals("SATURDAY")) {
                    logDetail.setSigns(signRepository.findByName(ESign.KL));
                }
            }
            if (hourOut != null && hourIn != null) {
                if ((hourIn == null && hourOut < 17) || (hourIn >= 10 && hourOut == null) || ((hourIn >= 8 && minuteIn >30) && hourOut < 17)) {
                    logDetail.setSigns(signRepository.findByName(ESign.KL));
                }

                if (hourIn >= 9  ) {
                    if(hourOut >= 17) logDetail.setSigns(signRepository.findByName(ESign.KL_H));
                    else if(hourOut < 17) logDetail.setSigns(signRepository.findByName(ESign.KL));
                }
                if (hourIn < 9) {
                    if(hourOut >= 17 && minuteOut >= 0){
                        logDetail.setSigns(signRepository.findByName(ESign.H));
                    } else if (hourOut >= 12 && hourOut < 17) {
                        logDetail.setSigns(signRepository.findByName(ESign.H_KL));
                    } else if (hourOut < 12) {
                        logDetail.setSigns(signRepository.findByName(ESign.KL));
                    }
                }
//                if(hourIn<10 &&  hourOut>15) {
//                    logDetail.setSigns(signRepository.findByName(ESign.H));
//                }

            }
            if (hourIn == null && hourOut != null) {
                if (hourOut < 17) {
                    logDetail.setSigns(signRepository.findByName(ESign.KL));
                }
                if (hourOut >= 17) {
                    logDetail.setSigns(signRepository.findByName(ESign.KL_H));
                }
            }
            if (hourIn != null && hourOut == null) {
                if (hourIn >= 9) {
                    logDetail.setSigns(signRepository.findByName(ESign.KL));
                }
                if (hourIn < 9) {
                    logDetail.setSigns(signRepository.findByName(ESign.H_KL));
                }
            }
            Set<NoteLog> noteCatergorySet = logDetail.getNoteLogSet();
            if (noteCatergorySet == null)
                noteCatergorySet = new HashSet<>();
            NoteLog noteLog = new NoteLog();
            noteLog.setLogDetail(logDetail);
            noteLog.setNoteCatergory(noteCatergoryRepository.findByName(ENoteCatergory.E_CHECKINOUT));
            noteLog.setLastSign(currentSign);
            noteLog.setCreateDate(LocalDateTime.now());
            noteLog.setSignChange(logDetail.getSigns());
//            noteCatergorySet.add(noteLog);
            logDetail.setNoteLogSet(noteCatergorySet);
            logDetailRepository.save(logDetail);
        }

        if (requests.size() > 0) {
            for (Request request : requests) {
                LogDetail  logDetail = logDetailRepository.findByUserCodeAndDate(request.getCreator().getCode(), yesterday);
                List<LogDetail>  logDetails1 = logDetailRepository.findByUserCodeAndDate(request.getCreator().getCode(),firstDayOfMonth, yesterday);
                Set<NoteLog> noteCatergorySet;
                NoteLog noteLog;
                if(!request.isAccess()){

                    if (logDetail != null ) {
                        Sign currentSign = logDetail.getSigns();
//                        LocalDate currentDay = LocalDate.now();
                        switch (request.getCategoryReason().getId().intValue()) {
                            //Nghỉ phép
                            case 1:
                                if (request.getDateFrom().equals(yesterday)) {
                                    if (request.getTimeStart().getHour() >= 13) {
                                        if (logDetail.getSigns() != null && !logDetail.getSigns().getName().toString().equals("H")) {
                                            if (logDetail.getSigns().getName().toString().startsWith("KL"))
                                                logDetail.setSigns(signRepository.findByName(ESign.KL_P));
                                            if (logDetail.getSigns().getName().toString().startsWith("H"))
                                                logDetail.setSigns(signRepository.findByName(ESign.H_P));
                                        } else logDetail.setSigns(signRepository.findByName(ESign.KL_P));

                                    } else
                                        logDetail.setSigns(signRepository.findByName(ESign.P));
                                }
                                if (request.getDateTo().equals(yesterday)) {
                                    if (request.getTimeEnd().getHour() > 13 || (request.getTimeEnd().getHour() == 13 && request.getTimeEnd().getMinute() > 0))
                                        logDetail.setSigns(signRepository.findByName(ESign.P));
                                    else {
                                        if (logDetail.getSigns() != null) {
                                            if (logDetail.getSigns().getName().toString().endsWith("KL"))
                                                logDetail.setSigns(signRepository.findByName(ESign.P_KL));
                                            if (logDetail.getSigns().getName().toString().endsWith("H"))
                                                logDetail.setSigns(signRepository.findByName(ESign.P_H));
                                        } else logDetail.setSigns(signRepository.findByName(ESign.P_KL));
                                    }
                                } else if (yesterday.isBefore(request.getDateTo())  && yesterday.isAfter(request.getDateFrom()))
                                    logDetail.setSigns(signRepository.findByName(ESign.P));

                                logDetail.setRequestActive(true);
                                noteCatergorySet = logDetail.getNoteLogSet();
                                if (noteCatergorySet == null)
                                    noteCatergorySet = new HashSet<>();
                                noteLog = new NoteLog();
                                noteLog.setLogDetail(logDetail);
                                noteLog.setNoteCatergory(noteCatergoryRepository.findByName(ENoteCatergory.E_REQUEST));
                                noteLog.setApproversRequest(request.getApproved());
                                noteLog.setContent(request.getContent());
                                noteLog.setLastSign(currentSign);
                                noteLog.setCreateDate(LocalDateTime.now());
                                noteLog.setSignChange(logDetail.getSigns());
                                noteCatergorySet.add(noteLog);
                                logDetail.setNoteLogSet(noteCatergorySet);
                                logDetailRepository.save(logDetail);
                                break;
                            // nghỉ ốm
                            case 3:
                                logDetail.setSigns(signRepository.findByName(ESign.Ô));
                                logDetail.setRequestActive(true);
                                noteCatergorySet = logDetail.getNoteLogSet();
                                if (noteCatergorySet == null)
                                    noteCatergorySet = new HashSet<>();
                                noteLog = new NoteLog();
                                noteLog.setLogDetail(logDetail);
                                noteLog.setNoteCatergory(noteCatergoryRepository.findByName(ENoteCatergory.E_REQUEST));
                                noteLog.setApproversRequest(request.getApproved());
                                noteLog.setContent(request.getContent());
                                noteLog.setLastSign(currentSign);
                                noteLog.setCreateDate(LocalDateTime.now());
                                noteLog.setSignChange(logDetail.getSigns());
                                noteCatergorySet.add(noteLog);
                                logDetail.setNoteLogSet(noteCatergorySet);
                                logDetailRepository.save(logDetail);
                                break;
                            //Nghỉ tiêu chuẩn
                            case 4:
                                logDetail.setSigns(signRepository.findByName(ESign.TC));
                                logDetail.setRequestActive(true);
                                noteCatergorySet = logDetail.getNoteLogSet();
                                if (noteCatergorySet == null)
                                    noteCatergorySet = new HashSet<>();
                                noteLog = new NoteLog();
                                noteLog.setLogDetail(logDetail);
                                noteLog.setNoteCatergory(noteCatergoryRepository.findByName(ENoteCatergory.E_REQUEST));
                                noteLog.setApproversRequest(request.getApproved());
                                noteLog.setContent(request.getContent());
                                noteLog.setLastSign(currentSign);
                                noteLog.setCreateDate(LocalDateTime.now());
                                noteLog.setSignChange(logDetail.getSigns());
                                noteCatergorySet.add(noteLog);
                                logDetail.setNoteLogSet(noteCatergorySet);
                                logDetailRepository.save(logDetail);
                                break;
                            //Nghỉ chế độ thai sản
                            case 5:
                                logDetail.setSigns(signRepository.findByName(ESign.CĐ));
                                logDetail.setRequestActive(true);
                                noteCatergorySet = logDetail.getNoteLogSet();
                                if (noteCatergorySet == null)
                                    noteCatergorySet = new HashSet<>();
                                noteLog = new NoteLog();
                                noteLog.setLogDetail(logDetail);
                                noteLog.setNoteCatergory(noteCatergoryRepository.findByName(ENoteCatergory.E_REQUEST));
                                noteLog.setApproversRequest(request.getApproved());
                                noteLog.setContent(request.getContent());
                                noteLog.setLastSign(currentSign);
                                noteLog.setCreateDate(LocalDateTime.now());
                                noteLog.setSignChange(logDetail.getSigns());
                                noteCatergorySet.add(noteLog);
                                logDetail.setNoteLogSet(noteCatergorySet);
                                logDetailRepository.save(logDetail);
                                break;
                            //Quên chấm công
                            case 6:
                                for (LogDetail log: logDetails1) {
                                    if(request.getDateForget().equals(log.getDateLog())){
                                        log.setSigns(signRepository.findByName(ESign.H));
                                        log.setRequestActive(true);
                                        noteCatergorySet = log.getNoteLogSet();
                                        if (noteCatergorySet == null)
                                            noteCatergorySet = new HashSet<>();
                                        noteLog = new NoteLog();
                                        noteLog.setLogDetail(log);
                                        noteLog.setNoteCatergory(noteCatergoryRepository.findByName(ENoteCatergory.E_REQUEST));
                                        noteLog.setApproversRequest(request.getApproved());
                                        noteLog.setContent(request.getContent());
                                        noteLog.setLastSign(currentSign);
                                        noteLog.setCreateDate(LocalDateTime.now());
                                        noteLog.setSignChange(log.getSigns());
                                        noteCatergorySet.add(noteLog);
                                        log.setNoteLogSet(noteCatergorySet);
                                        logDetailRepository.save(log);
                                    }
                                }

                                break;
                            //Work from home && Đi công tác
                            case 7:
                            case 8:
                                if (request.getDateFrom().equals(yesterday)) {
                                    if (request.getTimeStart().getHour() >= 13) {
                                        if (logDetail.getSigns() != null) {
                                            if (logDetail.getSigns().getName().toString().startsWith("KL"))
                                                logDetail.setSigns(signRepository.findByName(ESign.KL_H));
                                            if (logDetail.getSigns().getName().toString().startsWith("H"))
                                                logDetail.setSigns(signRepository.findByName(ESign.H));
                                        } else logDetail.setSigns(signRepository.findByName(ESign.KL_H));

                                    } else
                                        logDetail.setSigns(signRepository.findByName(ESign.H));
                                }

                                if (request.getDateTo().equals(yesterday)) {
                                    if (request.getTimeEnd().getHour() > 13 || (request.getTimeEnd().getHour() == 13 && request.getTimeEnd().getMinute() > 0))
                                        logDetail.setSigns(signRepository.findByName(ESign.H));
                                    else {
                                        if (logDetail.getSigns() != null) {
                                            if (logDetail.getSigns().getName().toString().endsWith("KL"))
                                                logDetail.setSigns(signRepository.findByName(ESign.H_KL));
                                            if (logDetail.getSigns().getName().toString().endsWith("H"))
                                                logDetail.setSigns(signRepository.findByName(ESign.H));
                                        } else logDetail.setSigns(signRepository.findByName(ESign.H_KL));
                                    }
                                } else if (yesterday != request.getDateTo() && yesterday != request.getDateFrom())
                                    logDetail.setSigns(signRepository.findByName(ESign.H));

                                logDetail.setRequestActive(true);
                                noteCatergorySet = logDetail.getNoteLogSet();
                                if (noteCatergorySet == null)
                                    noteCatergorySet = new HashSet<>();
                                noteLog = new NoteLog();
                                noteLog.setLogDetail(logDetail);
                                noteLog.setNoteCatergory(noteCatergoryRepository.findByName(ENoteCatergory.E_REQUEST));
                                noteLog.setApproversRequest(request.getApproved());
                                noteLog.setContent(request.getContent());
                                noteLog.setLastSign(currentSign);
                                noteLog.setCreateDate(LocalDateTime.now());
                                noteLog.setSignChange(logDetail.getSigns());
                                noteCatergorySet.add(noteLog);
                                logDetail.setNoteLogSet(noteCatergorySet);
                                logDetailRepository.save(logDetail);
                                break;
                            default:
                                break;
                        }

                    }

                    else {

//                        LocalDate currentDay = LocalDate.now();
                        LogDetail newLog = new LogDetail();
                        newLog.setUser(request.getCreator());

                        switch (request.getCategoryReason().getId().intValue()) {
                            //Nghỉ phép
                            case 1:
                                if (request.getDateFrom().equals(yesterday)) {
                                    newLog.setDateLog(yesterday);
                                    if (request.getTimeStart().getHour() >= 13) {
                                        newLog.setSigns(signRepository.findByName(ESign.KL_P));
                                    } else
                                        newLog.setSigns(signRepository.findByName(ESign.P));
                                }
                                if (request.getDateTo().equals(yesterday)) {
                                    newLog.setDateLog(yesterday);
                                    if (request.getTimeEnd().getHour() >= 13)
                                        newLog.setSigns(signRepository.findByName(ESign.P));

                                } else if (yesterday.isBefore(request.getDateTo())  && yesterday.isAfter(request.getDateFrom()))
                                    newLog.setDateLog(yesterday);
                                newLog.setSigns(signRepository.findByName(ESign.P));
                                break;
                            case 2:
                                newLog.setDateLog(yesterday);
                                newLog.setSigns(signRepository.findByName(ESign.KL));
                                break;
                            // nghỉ ốm
                            case 3:
                                newLog.setDateLog(request.getDateFrom());
                                newLog.setSigns(signRepository.findByName(ESign.Ô));
                                break;
                            //Nghỉ tiêu chuẩn
                            case 4:
                                newLog.setDateLog(yesterday);
                                newLog.setSigns(signRepository.findByName(ESign.TC));
                                break;
                            //Nghỉ chế độ thai sản
                            case 5:
                                newLog.setDateLog(yesterday);
                                newLog.setSigns(signRepository.findByName(ESign.CĐ));
                                break;
                            //Quên chấm công
                            case 6:
                                newLog.setSigns(signRepository.findByName(ESign.H));
                                newLog.setDateLog(request.getDateForget());
                                break;
                            //Work from home && Đi công tác
                            case 7:
                            case 8:
                                if (request.getDateFrom().equals(yesterday)) {
                                    newLog.setDateLog(yesterday);
                                    if (request.getTimeStart().getHour() >= 13) {
                                        if (logDetail.getSigns() == null) {
                                            newLog.setSigns(signRepository.findByName(ESign.KL_H));
                                        }
                                    } else
                                        newLog.setSigns(signRepository.findByName(ESign.KL_H));
                                }

                                if (request.getDateTo().equals(yesterday)) {
                                    newLog.setDateLog(yesterday);
                                    if (request.getTimeEnd().getHour() >= 13)
                                        newLog.setSigns(signRepository.findByName(ESign.H));
                                    else {
                                        newLog.setSigns(signRepository.findByName(ESign.H_KL));
                                    }
                                } else if (yesterday.isBefore(request.getDateTo())  && yesterday.isAfter(request.getDateFrom()))
                                    newLog.setDateLog(yesterday);
                                logDetail.setSigns(signRepository.findByName(ESign.H));
                                break;
                            default:
                                break;
                        }
                        newLog.setRequestActive(true);
                        noteCatergorySet = newLog.getNoteLogSet();
                        if (noteCatergorySet == null)
                            noteCatergorySet = new HashSet<>();
                        noteLog = new NoteLog();
                        noteLog.setLogDetail(newLog);
                        noteLog.setNoteCatergory(noteCatergoryRepository.findByName(ENoteCatergory.E_REQUEST));
                        noteLog.setApproversRequest(request.getApproved());
                        noteLog.setContent(request.getContent());
                        noteLog.setCreateDate(LocalDateTime.now());
//                    noteLog.setSignChange(logDetail.getSigns());
                        noteCatergorySet.add(noteLog);
                        newLog.setNoteLogSet(noteCatergorySet);
                        logDetailRepository.save(newLog);
                    }
                }
                request.setAccess(true);
                requestRepository.save(request);
            }


        }
        System.out.println("Chay ham tinh toan ki tu cham cong vao " + yesterday);
    }


    @Scheduled(cron = "0 00 18 * * *")
    public void convertSignFace() {
        LocalDate currentDate = LocalDate.now();
        LocalDate firstDayOfMonth = LocalDate.of(currentDate.getYear(), currentDate.getMonth(), 1);



        // Holiday CASE
        Holiday holiday = holidayRepository.findCurrentDate(currentDate.toString());
        if (holiday != null) {
            List<User> users = userRepository.findAll();
            for (User user : users) {
                System.out.println(user.getCode() + " Add Log HOLIDAY");
                // create log
                LogDetail logDetail = new LogDetail();
                Set<NoteLog> noteCatergorySet = logDetail.getNoteLogSet();
                if (noteCatergorySet == null)
                    noteCatergorySet = new HashSet<>();
                NoteLog noteLog = new NoteLog();
                noteLog.setLogDetail(logDetail);
                noteLog.setNoteCatergory(noteCatergoryRepository.findByName(ENoteCatergory.E_HOLIDAY));
                noteLog.setContent(holiday.getHolidayName());
                noteLog.setCreateDate(LocalDateTime.now());
                noteLog.setSignChange(signRepository.findByName(ESign.L));
                noteCatergorySet.add(noteLog);
                logDetail.setNoteLogSet(noteCatergorySet);
                logDetail.setSigns(signRepository.findByName(ESign.L));
                logDetail.setDateLog(currentDate);
                logDetail.setUser(user);
//                logDetail.setRequestActive(true);
                logDetailRepository.save(logDetail);
            }
            return;
        }

        // NT CASE
        if (currentDate.getDayOfWeek().toString() == "SATURDAY" ||
                currentDate.getDayOfWeek().toString() == "SUNDAY") {
            List<User> users = userRepository.findAll();
            for (User user : users) {
                System.out.println(user.getCode() + " Add Log SS");
                // create log
                LogDetail logDetail = new LogDetail();
                Set<NoteLog> noteCatergorySet = logDetail.getNoteLogSet();
                if (noteCatergorySet == null)
                    noteCatergorySet = new HashSet<>();
                NoteLog noteLog = new NoteLog();
                noteLog.setLogDetail(logDetail);
                noteLog.setNoteCatergory(noteCatergoryRepository.findByName(ENoteCatergory.E_WEEKEND));
//                noteLog.setContent(holiday.getHolidayName());
                noteLog.setCreateDate(LocalDateTime.now());
                noteLog.setSignChange(signRepository.findByName(ESign.NT));
                noteCatergorySet.add(noteLog);
                logDetail.setNoteLogSet(noteCatergorySet);
                logDetail.setSigns(signRepository.findByName(ESign.NT));
                logDetail.setDateLog(currentDate);
                logDetail.setUser(user);
//                logDetail.setRequestActive(true);
                logDetailRepository.save(logDetail);
            }
            return;
        }


        List<Request> requests = requestRepository.findByStatusAndDateList(2,firstDayOfMonth, currentDate);
        List<LogDetail> logDetails = logDetailRepository.findByCurrentDay(currentDate);

        for (LogDetail logDetail : logDetails) {
            Sign currentSign = logDetail.getSigns();
            if (holiday != null) {
                logDetail.setSigns(signRepository.findByName(ESign.L));
                Set<NoteLog> noteCatergorySet = logDetail.getNoteLogSet();
                if (noteCatergorySet == null)
                    noteCatergorySet = new HashSet<>();
                NoteLog noteLog = new NoteLog();
                noteLog.setLogDetail(logDetail);
                noteLog.setNoteCatergory(noteCatergoryRepository.findByName(ENoteCatergory.E_HOLIDAY));
                noteLog.setContent(holiday.getHolidayName());
                noteLog.setLastSign(logDetail.getSigns());
                noteLog.setCreateDate(LocalDateTime.now());
                noteLog.setSignChange(signRepository.findByName(ESign.L));
                noteCatergorySet.add(noteLog);
                logDetail.setNoteLogSet(noteCatergorySet);
                logDetail.setSigns(signRepository.findByName(ESign.L));
                logDetailRepository.save(logDetail);
                continue;
            }

            DayOfWeek dayOfWeek = logDetail.getDateLog().getDayOfWeek();
            Integer hourIn = null;
            Integer hourOut = null;
            Integer minuteIn = null;
            Integer minuteOut = null;
            Integer secondIn = null;
            Integer secondOut = null;
            if (logDetail.getTimeIn() != null) {
                hourIn = logDetail.getTimeIn().getHour();
                minuteIn = logDetail.getTimeIn().getMinute();
                secondIn = logDetail.getTimeIn().getSecond();
                System.out.println("Hour in : " + hourIn);
                System.out.println("Minute in : " + minuteIn);
                System.out.println("Second in : " + secondIn);

            }
            if (logDetail.getTimeOut() != null) {
                hourOut = logDetail.getTimeOut().getHour();
                minuteOut = logDetail.getTimeOut().getMinute();
                secondOut = logDetail.getTimeOut().getSecond();

                System.out.println("Hour out : " + hourOut);
                System.out.println("Minute out : " + minuteOut);
                System.out.println("Second out : " + secondOut);

            }
            System.out.println(logDetail.getDateLog() + "-" + dayOfWeek);

            if (hourOut == null && hourIn == null) {
                if (!dayOfWeek.toString().equals("SUNDAY") && !dayOfWeek.toString().equals("SATURDAY")) {
                    logDetail.setSigns(signRepository.findByName(ESign.KL));
                }
            }
            if (hourOut != null && hourIn != null) {
                if ((hourIn == null && hourOut < 17) || (hourIn >= 10 && hourOut == null) || ((hourIn >= 9) && hourOut < 17)) {
                    logDetail.setSigns(signRepository.findByName(ESign.KL));
                }
                if (hourIn >= 9 && hourOut >= 17) {
                    logDetail.setSigns(signRepository.findByName(ESign.KL_H));
                }
                if (hourIn < 9) {
                    if (hourOut >= 12 && hourOut < 17) {
                        logDetail.setSigns(signRepository.findByName(ESign.H_KL));
                    } else if (hourOut >= 17) {
                        logDetail.setSigns(signRepository.findByName(ESign.H));
                    }
                }
//                if(hourIn<10 &&  hourOut>15) {
//                    logDetail.setSigns(signRepository.findByName(ESign.H));
//                }

            }
            if (hourIn == null && hourOut != null) {
                if (hourOut < 17) {
                    logDetail.setSigns(signRepository.findByName(ESign.KL));
                }
                if (hourOut >= 17) {
                    logDetail.setSigns(signRepository.findByName(ESign.KL_H));
                }
            }
            if (hourIn != null && hourOut == null) {
                if (hourIn >= 9) {
                    logDetail.setSigns(signRepository.findByName(ESign.KL));
                }
                if (hourIn < 9) {
                    logDetail.setSigns(signRepository.findByName(ESign.H_KL));
                }
            }
            Set<NoteLog> noteCatergorySet = logDetail.getNoteLogSet();
            if (noteCatergorySet == null)
                noteCatergorySet = new HashSet<>();
            NoteLog noteLog = new NoteLog();
            noteLog.setLogDetail(logDetail);
            noteLog.setNoteCatergory(noteCatergoryRepository.findByName(ENoteCatergory.E_FACE));
            noteLog.setLastSign(currentSign);
            noteLog.setCreateDate(LocalDateTime.now());
            noteLog.setSignChange(logDetail.getSigns());
//            noteCatergorySet.add(noteLog);
            logDetail.setNoteLogSet(noteCatergorySet);
            logDetailRepository.save(logDetail);
        }

        if (requests.size() > 0) {
            for (Request request : requests) {
                LogDetail  logDetail = logDetailRepository.findByUserCodeAndDate(request.getCreator().getCode(), currentDate);
                List<LogDetail>  logDetails1 = logDetailRepository.findByUserCodeAndDate(request.getCreator().getCode(),firstDayOfMonth, currentDate);
                Set<NoteLog> noteCatergorySet;
                NoteLog noteLog;
                if(!request.isAccess()){

                    if (logDetail != null ) {
                        Sign currentSign = logDetail.getSigns();
                        LocalDate currentDay = LocalDate.now();
                        switch (request.getCategoryReason().getId().intValue()) {
                            //Nghỉ phép
                            case 1:
                                if (request.getDateFrom().equals(currentDay)) {
                                    if (request.getTimeStart().getHour() >= 13) {
                                        if (logDetail.getSigns() != null && !logDetail.getSigns().getName().toString().equals("H")) {
                                            if (logDetail.getSigns().getName().toString().startsWith("KL"))
                                                logDetail.setSigns(signRepository.findByName(ESign.KL_P));
                                            if (logDetail.getSigns().getName().toString().startsWith("H"))
                                                logDetail.setSigns(signRepository.findByName(ESign.H_P));
                                        } else logDetail.setSigns(signRepository.findByName(ESign.KL_P));

                                    } else
                                        logDetail.setSigns(signRepository.findByName(ESign.P));
                                }
                                if (request.getDateTo().equals(currentDay)) {
                                    if (request.getTimeEnd().getHour() > 13 || (request.getTimeEnd().getHour() == 13 && request.getTimeEnd().getMinute() > 0))
                                        logDetail.setSigns(signRepository.findByName(ESign.P));
                                    else {
                                        if (logDetail.getSigns() != null) {
                                            if (logDetail.getSigns().getName().toString().endsWith("KL"))
                                                logDetail.setSigns(signRepository.findByName(ESign.P_KL));
                                            if (logDetail.getSigns().getName().toString().endsWith("H"))
                                                logDetail.setSigns(signRepository.findByName(ESign.P_H));
                                        } else logDetail.setSigns(signRepository.findByName(ESign.P_KL));
                                    }
                                } else if (currentDay.isBefore(request.getDateTo())  && currentDay.isAfter(request.getDateFrom()))
                                    logDetail.setSigns(signRepository.findByName(ESign.P));

                                logDetail.setRequestActive(true);
                                noteCatergorySet = logDetail.getNoteLogSet();
                                if (noteCatergorySet == null)
                                    noteCatergorySet = new HashSet<>();
                                noteLog = new NoteLog();
                                noteLog.setLogDetail(logDetail);
                                noteLog.setNoteCatergory(noteCatergoryRepository.findByName(ENoteCatergory.E_REQUEST));
                                noteLog.setApproversRequest(request.getApproved());
                                noteLog.setContent(request.getContent());
                                noteLog.setLastSign(currentSign);
                                noteLog.setCreateDate(LocalDateTime.now());
                                noteLog.setSignChange(logDetail.getSigns());
                                noteCatergorySet.add(noteLog);
                                logDetail.setNoteLogSet(noteCatergorySet);
                                logDetailRepository.save(logDetail);
                                break;
                            // nghỉ ốm
                            case 3:
                                logDetail.setSigns(signRepository.findByName(ESign.Ô));
                                logDetail.setRequestActive(true);
                                noteCatergorySet = logDetail.getNoteLogSet();
                                if (noteCatergorySet == null)
                                    noteCatergorySet = new HashSet<>();
                                noteLog = new NoteLog();
                                noteLog.setLogDetail(logDetail);
                                noteLog.setNoteCatergory(noteCatergoryRepository.findByName(ENoteCatergory.E_REQUEST));
                                noteLog.setApproversRequest(request.getApproved());
                                noteLog.setContent(request.getContent());
                                noteLog.setLastSign(currentSign);
                                noteLog.setCreateDate(LocalDateTime.now());
                                noteLog.setSignChange(logDetail.getSigns());
                                noteCatergorySet.add(noteLog);
                                logDetail.setNoteLogSet(noteCatergorySet);
                                logDetailRepository.save(logDetail);
                                break;
                            //Nghỉ tiêu chuẩn
                            case 4:
                                logDetail.setSigns(signRepository.findByName(ESign.TC));
                                logDetail.setRequestActive(true);
                                noteCatergorySet = logDetail.getNoteLogSet();
                                if (noteCatergorySet == null)
                                    noteCatergorySet = new HashSet<>();
                                noteLog = new NoteLog();
                                noteLog.setLogDetail(logDetail);
                                noteLog.setNoteCatergory(noteCatergoryRepository.findByName(ENoteCatergory.E_REQUEST));
                                noteLog.setApproversRequest(request.getApproved());
                                noteLog.setContent(request.getContent());
                                noteLog.setLastSign(currentSign);
                                noteLog.setCreateDate(LocalDateTime.now());
                                noteLog.setSignChange(logDetail.getSigns());
                                noteCatergorySet.add(noteLog);
                                logDetail.setNoteLogSet(noteCatergorySet);
                                logDetailRepository.save(logDetail);
                                break;
                            //Nghỉ chế độ thai sản
                            case 5:
                                logDetail.setSigns(signRepository.findByName(ESign.CĐ));
                                logDetail.setRequestActive(true);
                                noteCatergorySet = logDetail.getNoteLogSet();
                                if (noteCatergorySet == null)
                                    noteCatergorySet = new HashSet<>();
                                noteLog = new NoteLog();
                                noteLog.setLogDetail(logDetail);
                                noteLog.setNoteCatergory(noteCatergoryRepository.findByName(ENoteCatergory.E_REQUEST));
                                noteLog.setApproversRequest(request.getApproved());
                                noteLog.setContent(request.getContent());
                                noteLog.setLastSign(currentSign);
                                noteLog.setCreateDate(LocalDateTime.now());
                                noteLog.setSignChange(logDetail.getSigns());
                                noteCatergorySet.add(noteLog);
                                logDetail.setNoteLogSet(noteCatergorySet);
                                logDetailRepository.save(logDetail);
                                break;
                            //Quên chấm công
                            case 6:
                                for (LogDetail log: logDetails1) {
                                    if(request.getDateForget().equals(log.getDateLog())){
                                        log.setSigns(signRepository.findByName(ESign.H));
                                        log.setRequestActive(true);
                                        noteCatergorySet = log.getNoteLogSet();
                                        if (noteCatergorySet == null)
                                            noteCatergorySet = new HashSet<>();
                                        noteLog = new NoteLog();
                                        noteLog.setLogDetail(log);
                                        noteLog.setNoteCatergory(noteCatergoryRepository.findByName(ENoteCatergory.E_REQUEST));
                                        noteLog.setApproversRequest(request.getApproved());
                                        noteLog.setContent(request.getContent());
                                        noteLog.setLastSign(currentSign);
                                        noteLog.setCreateDate(LocalDateTime.now());
                                        noteLog.setSignChange(log.getSigns());
                                        noteCatergorySet.add(noteLog);
                                        log.setNoteLogSet(noteCatergorySet);
                                        logDetailRepository.save(log);
                                    }
                                }

                                break;
                            //Work from home && Đi công tác
                            case 7:
                            case 8:
                                if (request.getDateFrom().equals(currentDay)) {
                                    if (request.getTimeStart().getHour() >= 13) {
                                        if (logDetail.getSigns() != null) {
                                            if (logDetail.getSigns().getName().toString().startsWith("KL"))
                                                logDetail.setSigns(signRepository.findByName(ESign.KL_H));
                                            if (logDetail.getSigns().getName().toString().startsWith("H"))
                                                logDetail.setSigns(signRepository.findByName(ESign.H));
                                        } else logDetail.setSigns(signRepository.findByName(ESign.KL_H));

                                    } else
                                        logDetail.setSigns(signRepository.findByName(ESign.H));
                                }

                                if (request.getDateTo().equals(currentDay)) {
                                    if (request.getTimeEnd().getHour() > 13 || (request.getTimeEnd().getHour() == 13 && request.getTimeEnd().getMinute() > 0))
                                        logDetail.setSigns(signRepository.findByName(ESign.H));
                                    else {
                                        if (logDetail.getSigns() != null) {
                                            if (logDetail.getSigns().getName().toString().endsWith("KL"))
                                                logDetail.setSigns(signRepository.findByName(ESign.H_KL));
                                            if (logDetail.getSigns().getName().toString().endsWith("H"))
                                                logDetail.setSigns(signRepository.findByName(ESign.H));
                                        } else logDetail.setSigns(signRepository.findByName(ESign.H_KL));
                                    }
                                } else if (currentDay != request.getDateTo() && currentDay != request.getDateFrom())
                                    logDetail.setSigns(signRepository.findByName(ESign.H));

                                logDetail.setRequestActive(true);
                                noteCatergorySet = logDetail.getNoteLogSet();
                                if (noteCatergorySet == null)
                                    noteCatergorySet = new HashSet<>();
                                noteLog = new NoteLog();
                                noteLog.setLogDetail(logDetail);
                                noteLog.setNoteCatergory(noteCatergoryRepository.findByName(ENoteCatergory.E_REQUEST));
                                noteLog.setApproversRequest(request.getApproved());
                                noteLog.setContent(request.getContent());
                                noteLog.setLastSign(currentSign);
                                noteLog.setCreateDate(LocalDateTime.now());
                                noteLog.setSignChange(logDetail.getSigns());
                                noteCatergorySet.add(noteLog);
                                logDetail.setNoteLogSet(noteCatergorySet);
                                logDetailRepository.save(logDetail);
                                break;
                            default:
                                break;
                        }

                    }

                    else {

                        LocalDate currentDay = LocalDate.now();
                        LogDetail newLog = new LogDetail();
                        newLog.setUser(request.getCreator());

                        switch (request.getCategoryReason().getId().intValue()) {
                            //Nghỉ phép
                            case 1:
                                if (request.getDateFrom().equals(currentDay)) {
                                    newLog.setDateLog(currentDay);
                                    if (request.getTimeStart().getHour() >= 13) {
                                        newLog.setSigns(signRepository.findByName(ESign.KL_P));
                                    } else
                                        newLog.setSigns(signRepository.findByName(ESign.P));
                                }
                                if (request.getDateTo().equals(currentDay)) {
                                    newLog.setDateLog(currentDay);
                                    if (request.getTimeEnd().getHour() >= 13)
                                        newLog.setSigns(signRepository.findByName(ESign.P));

                                } else if (currentDay.isBefore(request.getDateTo())  && currentDay.isAfter(request.getDateFrom()))
                                    newLog.setDateLog(currentDay);
                                newLog.setSigns(signRepository.findByName(ESign.P));
                                break;
                            case 2:
                                newLog.setDateLog(currentDay);
                                newLog.setSigns(signRepository.findByName(ESign.KL));
                                break;
                            // nghỉ ốm
                            case 3:
                                newLog.setDateLog(request.getDateFrom());
                                newLog.setSigns(signRepository.findByName(ESign.Ô));
                                break;
                            //Nghỉ tiêu chuẩn
                            case 4:
                                newLog.setDateLog(currentDay);
                                newLog.setSigns(signRepository.findByName(ESign.TC));
                                break;
                            //Nghỉ chế độ thai sản
                            case 5:
                                newLog.setDateLog(currentDay);
                                newLog.setSigns(signRepository.findByName(ESign.CĐ));
                                break;
                            //Quên chấm công
                            case 6:
                                newLog.setSigns(signRepository.findByName(ESign.H));
                                newLog.setDateLog(request.getDateForget());
                                break;
                            //Work from home && Đi công tác
                            case 7:
                            case 8:
                                if (request.getDateFrom().equals(currentDay)) {
                                    newLog.setDateLog(currentDay);
                                    if (request.getTimeStart().getHour() >= 13) {
                                        if (logDetail.getSigns() == null) {
                                            newLog.setSigns(signRepository.findByName(ESign.KL_H));
                                        }
                                    } else
                                        newLog.setSigns(signRepository.findByName(ESign.KL_H));
                                }

                                if (request.getDateTo().equals(currentDay)) {
                                    newLog.setDateLog(currentDay);
                                    if (request.getTimeEnd().getHour() >= 13)
                                        newLog.setSigns(signRepository.findByName(ESign.H));
                                    else {
                                        newLog.setSigns(signRepository.findByName(ESign.H_KL));
                                    }
                                } else if (currentDay.isBefore(request.getDateTo())  && currentDay.isAfter(request.getDateFrom()))
                                    newLog.setDateLog(currentDay);
                                logDetail.setSigns(signRepository.findByName(ESign.H));
                                break;
                            default:
                                break;
                        }
                        newLog.setRequestActive(true);
                        noteCatergorySet = newLog.getNoteLogSet();
                        if (noteCatergorySet == null)
                            noteCatergorySet = new HashSet<>();
                        noteLog = new NoteLog();
                        noteLog.setLogDetail(newLog);
                        noteLog.setNoteCatergory(noteCatergoryRepository.findByName(ENoteCatergory.E_REQUEST));
                        noteLog.setApproversRequest(request.getApproved());
                        noteLog.setContent(request.getContent());
                        noteLog.setCreateDate(LocalDateTime.now());
//                    noteLog.setSignChange(logDetail.getSigns());
                        noteCatergorySet.add(noteLog);
                        newLog.setNoteLogSet(noteCatergorySet);
                        logDetailRepository.save(newLog);
                    }
                }
                request.setAccess(true);
                requestRepository.save(request);
            }


        }
        System.out.println("Chay ham tinh toan ki tu cham cong vao " + currentDate);
    }
}
