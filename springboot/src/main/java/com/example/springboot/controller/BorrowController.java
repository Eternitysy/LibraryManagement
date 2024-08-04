package com.example.springboot.controller;

import com.example.springboot.common.Result;
import com.example.springboot.controller.request.BorrowPageRequest;
import com.example.springboot.entity.Borrow;
import com.example.springboot.entity.Returns;
import com.example.springboot.service.IBorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/borrow")
public class BorrowController {
    @Autowired
    IBorrowService borrowService;

    @PostMapping("/save")
    public Result save(@RequestBody Borrow borrow) {
        borrowService.save(borrow);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody Borrow borrow) {
        borrowService.update(borrow);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        Borrow borrow = borrowService.getById(id);
        return Result.success(borrow);
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        borrowService.delete(id);
        return Result.success();
    }

    @GetMapping("/list")
    public Result list() {
        List<Borrow> borrow = borrowService.list();
        return Result.success(borrow);
    }

    @GetMapping("/page")
    public Result page(BorrowPageRequest borrowPageRequest) {
        return Result.success(borrowService.page(borrowPageRequest));
    }

    @GetMapping("/pageReturns")
    public Result pageReturns(BorrowPageRequest borrowPageRequest) {
        return Result.success(borrowService.pageReturns(borrowPageRequest));
    }

    @PostMapping("/saveReturns")
    public Result saveReturns(@RequestBody Returns returns) {
        borrowService.saveReturns(returns);
        return Result.success();
    }

    @DeleteMapping("/deleteReturns/{id}")
    public Result deleteReturns(@PathVariable Integer id) {
        borrowService.deleteReturns(id);
        return Result.success();
    }

    @GetMapping("/lineCharts/{timeRange}")
    public Result lineCharts(@PathVariable String timeRange) {
        return Result.success(borrowService.getCountByTimeRange(timeRange));
    }
}
