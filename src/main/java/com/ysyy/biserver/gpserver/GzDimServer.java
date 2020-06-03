package com.ysyy.biserver.gpserver;

import com.ysyy.biserver.gpmodel.GzDim;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GzDimServer {
    List<GzDim> GetList();
}
