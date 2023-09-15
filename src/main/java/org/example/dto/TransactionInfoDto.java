package org.example.dto;

import java.util.Map;
import java.util.Objects;

public class TransactionInfoDto {
    private Long customerId;
    private String firstName;
    private String lastName;
    private String phoneNumber;

    private int totalPoints;

    private Map<String, Integer> pointsByMonth;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public Map<String, Integer> getPointsByMonth() {
        return pointsByMonth;
    }

    public void setPointsByMonth(Map<String, Integer> pointsByMonth) {
        this.pointsByMonth = pointsByMonth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionInfoDto that = (TransactionInfoDto) o;
        return Objects.equals(customerId, that.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId);
    }
}
