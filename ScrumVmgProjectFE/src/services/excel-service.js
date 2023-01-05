// import httpCommon from "@/http-common";
import axios from 'axios';


class ExcelService{
    exportExcelReport(params){
        axios.get(`http://localhost:8080/api/excel/export_report`, {params,
            responseType: 'blob',
        }).then((response) => {
            const url = URL.createObjectURL(new Blob([response.data]))
            const link = document.createElement('a')
            link.href = url
            link.setAttribute(
                'download',
                `Bảng Chấm Công-${new Date().toLocaleDateString()}.xlsx`
            )
            document.body.appendChild(link)
            link.click()
        })
    }
    exportExcelPhep(params){
        axios.get(`http://localhost:8080/api/excel/export_phep`, {params,
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
    exportExcelUser(params){
        axios.get(`http://localhost:8080/api/excel/export_users`, {params,
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
    importUser(data){
        let formData = new FormData(data);
        console.log(formData)
        return axios.post("http://localhost:8080/api/excel/import/user", formData)
    }
    importLog(data){
        let formData = new FormData(data);
        console.log(formData)
        return axios.post("http://localhost:8080/api/excel/import", formData)
    }
}

export default new ExcelService()