package com.ysyy.biserver.gpserver;


import com.ysyy.biserver.gpmodel.ResTotalbydayareao2O;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface ResTotalbydayareao2OServer {
    List<ResTotalbydayareao2O> GetList();
}
