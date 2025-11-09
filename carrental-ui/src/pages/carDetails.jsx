// src/CarDetails.jsx
import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";

export default function CarDetails() {
    const { carId } = useParams();
    const [carData, setCarData] = useState(null);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        axios.get(`http://localhost:8080/api/v1/cars/${carId}`)
            .then(response => {
                setCarData(response.data);
                setLoading(false);
            })
            .catch(error => {
                console.error("Error fetching car details:", error);
                setLoading(false);
            });
    }, [carId]);

    const handleReserve = async () => {
        try {
            const response = await axios.post(
                `http://localhost:8080/api/v1/cars/${carId}/rent`,
                {},
                {
                    headers: {
                        Authorization: "Bearer dummy-token" // mock for now
                    }
                }
            );
            const { paymentLink } = response.data;
            navigate(`/payment?link=${encodeURIComponent(paymentLink)}`);
        } catch (error) {
            console.error("Error reserving car:", error);
            alert("Error reserving car");
        }
    };

    if (loading) return <p>Loading...</p>;
    if (!carData) return <p>No data available</p>;

    const { car, available, currentRate } = carData;

    return (
        <div style={{ padding: "20px" }}>
            <h2>{car.manufacturer} {car.model} ({car.year})</h2>
            <p><b>Type:</b> {car.type}</p>
            <p><b>Available:</b> {available ? "Yes" : "No"}</p>
            <p><b>Current Rate:</b> ${currentRate} / day</p>

            {available ? (
                <button onClick={handleReserve}>Reserve Car</button>
            ) : (
                <p style={{ color: "red" }}>Not available right now</p>
            )}

            <h3>Reviews:</h3>
            {car.reviews && car.reviews.length > 0 ? (
                car.reviews.map((r, i) => (
                    <div key={i}>⭐ {r.score} — "{r.comment}"</div>
                ))
            ) : (
                <p>No reviews yet</p>
            )}
        </div>
    );
}
