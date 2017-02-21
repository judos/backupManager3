package ch.judos.backupManager.view;

import java.awt.Component;

import org.bridj.Pointer;
import org.bridj.cpp.com.COMRuntime;
import org.bridj.cpp.com.shell.ITaskbarList3;
import org.bridj.cpp.com.shell.ITaskbarList3.TbpFlag;
import org.bridj.jawt.JAWTUtils;

public class TaskBarProgressWrapper {

	private ITaskbarList3 list;
	private Pointer<Integer> windowHandlePointer;

	public enum State {
		NO_PROGRESS(TbpFlag.TBPF_NOPROGRESS), INDETERMINED(TbpFlag.TBPF_INDETERMINATE),
		NORMAL(TbpFlag.TBPF_NORMAL), PAUSED(TbpFlag.TBPF_PAUSED), ERROR(TbpFlag.TBPF_ERROR);

		private TbpFlag flag;

		private State(TbpFlag flag) {
			this.flag = flag;
		}

		public TbpFlag getFlag() {
			return this.flag;
		}
	}

	@SuppressWarnings({"deprecation", "unchecked"})
	public TaskBarProgressWrapper(Component component) {
		try {
			this.list = COMRuntime.newInstance(ITaskbarList3.class);
			long hwndVal = JAWTUtils.getNativePeerHandle(component);
			this.windowHandlePointer = (Pointer<Integer>) Pointer.pointerToAddress(hwndVal);
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		this.list.Release();
	}

	public void setProgress(double progress) {
		this.list.SetProgressValue((Pointer<Integer>) this.windowHandlePointer, (int) (progress
			* 1000), 1000);
	}

	public void setState(State state) {
		this.list.SetProgressState((Pointer<Integer>) this.windowHandlePointer, state
			.getFlag());
	}
}
