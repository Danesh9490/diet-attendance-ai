let currentUser = null;

function loadAuthenticatedUser(expectedRole){

    const stored =
        sessionStorage.getItem("authenticatedUser");

    if(!stored){

        window.location.href = "/login.html";
        return false;
    }

    try{

        currentUser = JSON.parse(stored);

    }catch(error){

        sessionStorage.clear();
        window.location.href = "/login.html";
        return false;
    }

    if(
        expectedRole &&
        currentUser.role !== expectedRole
    ){

        alert("You are not authorized for this portal.");

        window.location.href = "/login.html";

        return false;
    }

    updateUserInterface();

    return true;
}


function updateUserInterface(){

    const name =
        document.querySelectorAll("[data-user-name]");

    const id =
        document.querySelectorAll("[data-user-id]");

    const department =
        document.querySelectorAll("[data-user-department]");

    const role =
        document.querySelectorAll("[data-user-role]");

    const email =
        document.querySelectorAll("[data-user-email]");

    name.forEach(
        element =>
        element.textContent =
        currentUser.name || "User"
    );

    id.forEach(
        element =>
        element.textContent =
        currentUser.id || "-"
    );

    department.forEach(
        element =>
        element.textContent =
        currentUser.department || "-"
    );

    role.forEach(
        element =>
        element.textContent =
        currentUser.role || "-"
    );

    email.forEach(
        element =>
        element.textContent =
        currentUser.email || "-"
    );
}


async function runAI(){

    const result =
        document.getElementById("aiResult");

    const buttons =
        document.querySelectorAll(".ai-button");

    if(!currentUser){

        result.textContent =
            "Login session not found.";

        return;
    }

    buttons.forEach(
        button => {

            button.disabled = true;

            button.textContent =
                "AI ANALYZING...";

        }
    );

    result.textContent =
        "✦ DIET AI is analyzing your academic profile...";

    try{

        const response =
            await fetch(
                "/api/ai/analyze/"
                +
                encodeURIComponent(
                    currentUser.id
                )
                +
                "?t="
                +
                Date.now()
            );

        const data =
            await response.json();

        if(!response.ok){

            throw new Error(
                data.message ||
                "AI service failed."
            );
        }

        result.textContent =
            data.analysis;

    }catch(error){

        console.error(error);

        result.textContent =
            "AI ANALYSIS ERROR\n\n"
            +
            error.message
            +
            "\n\nMake sure Spring Boot is running.";

    }finally{

        buttons.forEach(
            button => {

                button.disabled = false;

                button.textContent =
                    "✦ ANALYZE WITH AI";

            }
        );
    }
}


async function loadStudentAttendance(){

    if(!currentUser){
        return;
    }

    const table =
        document.getElementById(
            "attendanceTable"
        );

    if(!table){
        return;
    }

    try{

        const response =
            await fetch(
                "/api/academic/student/"
                +
                encodeURIComponent(
                    currentUser.id
                )
                +
                "/attendance"
            );

        const data =
            await response.json();

        if(!response.ok){
            throw new Error(
                "Attendance could not be loaded."
            );
        }

        table.innerHTML = "";

        data.forEach(
            item => {

                const percentage =
                    Number(
                        item.percentage
                    ).toFixed(1);

                let status =
                    "good";

                if(percentage < 75){
                    status = "danger";
                }else if(percentage < 85){
                    status = "warning";
                }

                table.innerHTML +=
                    `
                    <tr>
                        <td>${item.subject}</td>
                        <td>${item.attended}</td>
                        <td>${item.conducted}</td>
                        <td>${percentage}%</td>
                        <td>
                            <span class="badge ${status}">
                                ${
                                    percentage < 75
                                    ? "Needs Attention"
                                    : percentage < 85
                                    ? "Watch"
                                    : "Good"
                                }
                            </span>
                        </td>
                    </tr>
                    `;
            }
        );

    }catch(error){

        table.innerHTML =
            `
            <tr>
                <td colspan="5">
                    ${error.message}
                </td>
            </tr>
            `;
    }
}


function showSection(title, message){

    const result =
        document.getElementById("aiResult");

    if(result){

        result.textContent =
            title
            +
            "\n\n"
            +
            message;
    }

    window.scrollTo({
        top:document.body.scrollHeight,
        behavior:"smooth"
    });
}


function logout(){

    sessionStorage.clear();

    window.location.href =
        "/login.html";
}


function toggleSidebar(){

    const sidebar =
        document.querySelector(".sidebar");

    if(sidebar){

        sidebar.classList.toggle("open");
    }
}