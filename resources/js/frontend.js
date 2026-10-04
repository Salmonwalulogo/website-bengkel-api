document.querySelectorAll(".nav-toggle").forEach((toggle) => {
    const navbar = toggle.closest(".navbar");
    const links = navbar?.querySelector(".nav-links");

    if (!links) {
        return;
    }

    const closeMenu = () => {
        links.classList.remove("open");
        toggle.setAttribute("aria-expanded", "false");
        toggle.setAttribute("aria-label", "Buka menu navigasi");
        toggle.querySelector("i")?.classList.replace("bi-x-lg", "bi-list");
    };

    toggle.addEventListener("click", () => {
        const isOpen = links.classList.toggle("open");
        toggle.setAttribute("aria-expanded", String(isOpen));
        toggle.setAttribute(
            "aria-label",
            isOpen ? "Tutup menu navigasi" : "Buka menu navigasi",
        );
        toggle.querySelector("i")?.classList.toggle("bi-list", !isOpen);
        toggle.querySelector("i")?.classList.toggle("bi-x-lg", isOpen);
    });

    links.addEventListener("click", (event) => {
        if (event.target instanceof Element && event.target.closest("a")) {
            closeMenu();
        }
    });

    document.addEventListener("keydown", (event) => {
        if (event.key === "Escape") {
            closeMenu();
        }
    });
});
