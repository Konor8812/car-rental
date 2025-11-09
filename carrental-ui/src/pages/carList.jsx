import { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { getToken } from "../api";

export default function CarList() {
    const [cars, setCars] = useState([]);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        const token = getToken();
        if (!token) {
            navigate("/login");
            return;
        }

        axios
            .get("http://localhost:8080/api/v1/cars", {
                headers: { Authorization: `Bearer ${token}` },
            })
            .then((res) => {
                setCars(res.data.cars);
            })
            .catch((err) => {
                console.error("Error fetching cars:", err);
            })
            .finally(() => setLoading(false));
    }, [navigate]);

    if (loading) return <p>Loading...</p>;

    return (
        <div style={{ padding: "20px" }}>
            <h1>Available Cars</h1>
            <button onClick={() => navigate("/")}>🏠 Back to Main</button>
            <table border="1" cellPadding="10" style={{ marginTop: "10px" }}>
                <thead>
                <tr>
                    <th>Type</th>
                    <th>Manufacturer</th>
                    <th>Model</th>
                    <th>Year</th>
                    <th>Reviews</th>
                    <th>Details</th>
                </tr>
                </thead>
                <tbody>
                {cars.map((car, i) => (
                    <tr key={i}>
                        <td>{car.type}</td>
                        <td>{car.manufacturer}</td>
                        <td>{car.model}</td>
                        <td>{car.year}</td>
                        <td>
                            {car.reviews && car.reviews.length > 0
                                ? car.reviews.map((r, j) => (
                                    <div key={j}>⭐ {r.score} — "{r.comment}"</div>
                                ))
                                : "No reviews"}
                        </td>
                        <td>
                            <button onClick={() => navigate(`/cars/${car.id}`)}>+</button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}
