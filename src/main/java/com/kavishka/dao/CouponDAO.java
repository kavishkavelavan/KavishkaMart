package com.kavishka.dao;

import com.kavishka.kavishkamart.util.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CouponDAO {

    public int getDiscountPercent(String code) throws SQLException {
        if (code == null || code.trim().isEmpty()) return 0;
        String sql = "SELECT discount_percent FROM coupons WHERE UPPER(code) = UPPER(?)";
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, code.trim());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("discount_percent");
                }
            }
        }
        return 0;
    }
}
