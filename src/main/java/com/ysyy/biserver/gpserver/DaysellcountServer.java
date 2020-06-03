package com.ysyy.biserver.gpserver;

import com.ysyy.biserver.gpmodel.Daysellcount;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface DaysellcountServer {
    List<Daysellcount> GetList();
}
