import React, { useEffect, useState, useCallback } from "react";
import { getLeaveList } from './api/leave';
import { Table, Button, Form, InputGroup } from 'react-bootstrap';
import { ChevronLeft, ChevronRight, X } from 'lucide-react';
import 'bootstrap/dist/css/bootstrap.min.css';

const LeaveList2 = () => {
    const [leaves, setLeaves] = useState([]);
    const [date, setDate] = useState(getCurrentMonth());
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState(null);

    const fetchLeaves = useCallback(() => {
        setIsLoading(true);
        setError(null);
        const params = date ? { start: `${date}-01` } : {};
        getLeaveList(params)
            .then(res => {
                setLeaves(res.data || []);
                setIsLoading(false);
            })
            .catch(err => {
                console.error('Error fetching leaves:', err);
                setError('Failed to fetch leaves. Please try again.');
                setIsLoading(false);
            });
    }, [date]);

    useEffect(() => {
        fetchLeaves();
    }, [fetchLeaves]);

    const handleDateChange = (e) => setDate(e.target.value);
    const clearDate = () => setDate('');
    const changeMonth = (offset) => {
        const newDate = new Date(date + '-01');
        newDate.setMonth(newDate.getMonth() + offset);
        setDate(formatDate(newDate));
    };

    const titleStyle = {
        background: 'linear-gradient(45deg, #4158D0, #C850C0)',
        color: 'white',
        padding: '15px',
        borderRadius: '10px',
        boxShadow: '0 4px 6px rgba(0, 0, 0, 0.1)',
        marginBottom: '20px',
        textAlign: 'center'
    };

    return (
        <div className="container my-5">
            <div style={titleStyle}>
                <h2 className="mb-0">Employee Leave Dashboard</h2>
            </div>
            <DatePicker
                date={date}
                onDateChange={handleDateChange}
                onPrevMonth={() => changeMonth(-1)}
                onNextMonth={() => changeMonth(1)}
                onClear={clearDate}
            />
            {isLoading && <p className="text-center">Loading...</p>}
            {error && <p className="text-danger text-center">{error}</p>}
            {!isLoading && !error && (
                <LeaveTable leaves={leaves} />
            )}
        </div>
    );
};

const DatePicker = ({ date, onDateChange, onPrevMonth, onNextMonth, onClear }) => (
    <Form className="mb-4">
        <Form.Group>
            <Form.Label htmlFor="datePicker" className="text-muted">Select Month:</Form.Label>
            <InputGroup>
                <Button variant="outline-secondary" onClick={onPrevMonth}>
                    <ChevronLeft size={18} />
                </Button>
                <Form.Control
                    type="month"
                    id="datePicker"
                    value={date}
                    onChange={onDateChange}
                />
                <Button variant="outline-secondary" onClick={onNextMonth}>
                    <ChevronRight size={18} />
                </Button>
                <Button variant="outline-danger" onClick={onClear}>
                    <X size={18} />
                </Button>
            </InputGroup>
        </Form.Group>
    </Form>
);

const LeaveTable = ({ leaves }) => (
    <Table responsive striped bordered hover>
        <thead className="bg-light">
            <tr>
                <th>#</th>
                <th>Employee</th>
                <th>Start Date</th>
                <th>End Date</th>
                <th>Reason</th>
                <th>Status</th>
                <th>Type</th>
                <th>Comment</th>
            </tr>
        </thead>
        <tbody>
            {leaves.map((leave, index) => (
                <tr key={leave.id}>
                    <td>{index + 1}</td>
                    <td>{leave.employee.name}</td>
                    <td>{formatDate(new Date(leave.start))}</td>
                    <td>{formatDate(new Date(leave.end))}</td>
                    <td>{leave.reasons}</td>
                    <td>
                        <LeaveStatus status={leave.leaveStatus} />
                    </td>
                    <td>{leave.entitlement.leaveType}</td>
                    <td>{leave.comment || '-'}</td>
                </tr>
            ))}
        </tbody>
    </Table>
);

const LeaveStatus = ({ status }) => {
    let badgeClass = 'badge ';
    switch (status.toLowerCase()) {
        case 'approved':
            badgeClass += 'bg-success';
            break;
        case 'pending':
            badgeClass += 'bg-warning text-dark';
            break;
        case 'rejected':
            badgeClass += 'bg-danger';
            break;
        default:
            badgeClass += 'bg-secondary';
    }
    return <span className={badgeClass}>{status}</span>;
};

function getCurrentMonth() {
    const now = new Date();
    return formatDate(now);
}

function formatDate(date) {
    return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}`;
}

export default LeaveList2;