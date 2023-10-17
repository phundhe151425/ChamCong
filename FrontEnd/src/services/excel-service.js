// import httpCommon from "@/http-common";
import axios from 'axios';
import { BASE_URL } from "@/http-common";

class ExcelService {
    exportExcelReport(params) {
        axios.get(BASE_URL + `/excel/export_report`, {
            params,
            responseType: 'blob',
        }).then((response) => {
            const url = URL.createObjectURL(new Blob([response.data]))
            const link = document.createElement('a')
            link.href = url
            link.setAttribute(
                'download',
                `Bảng Chấm Công-${new Date().toLocaleDateString()}.xls`
            )
            document.body.appendChild(link)
            link.click()
        })
    }

    exportExcelPhep(params) {
        axios.get(BASE_URL + `/excel/export_phep`, {
            params,
            responseType: 'blob',
        }).then((response) => {
            const url = URL.createObjectURL(new Blob([response.data]))
            const link = document.createElement('a')
            link.href = url
            link.setAttribute(
                'download',
                `Bảng Ngày Phép -${new Date().toLocaleDateString()}.xlsx`
            )
            document.body.appendChild(link)
            link.click()
        })
    }

    exportExcelUser(params) {
        axios.get(BASE_URL + `/excel/export_users`, {
            params,
            responseType: 'blob',
        }).then((response) => {
            const url = URL.createObjectURL(new Blob([response.data]))
            const link = document.createElement('a')
            link.href = url
            link.setAttribute(
                'download',
                `Danh sách nhân viên -${new Date().toLocaleDateString()}.xlsx`
            )
            document.body.appendChild(link)
            link.click()
        })
    }

    importUser(data) {
        let formData = new FormData(data);
        console.log(formData)
        return axios.post(BASE_URL + "/excel/import/user", formData)
    }

    importLog(data) {
        let formData = new FormData(data);
        console.log(formData)
        return axios.post(BASE_URL + "/excel/import", formData)
    }
}

export default new ExcelService()