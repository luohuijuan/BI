package com.ysyy.biserver.gpserver;

import com.ysyy.biserver.gpmodel.Resymdweek;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ResymdweekServer {
    List<Resymdweek> GetList();
}
